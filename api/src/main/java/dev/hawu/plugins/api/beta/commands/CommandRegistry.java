package dev.hawu.plugins.api.beta.commands;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.beta.reflect.LambdaFactory;
import dev.hawu.plugins.api.commands.CommandArgument;
import dev.hawu.plugins.api.commands.CommandSource;
import dev.hawu.plugins.api.commands.SenderType;
import dev.hawu.plugins.api.misc.ConsoleLogger;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * The registry to register commands made
 * with this library.
 *
 * @since 2.0
 */
public final class CommandRegistry {

    private static final Map<String, BiConsumer<?, ?>> FUNCTIONS = new HashMap<>();
    private static final Map<String, BiFunction<?, ?, ?>> TAB_COMPLETES = new HashMap<>();

    @NotNull
    private static Method findBaseCommand(final @NotNull Method[] methods, final @NotNull String name) {
        Method base = null;

        for(final Method method : methods) {
            if(!method.isAnnotationPresent(Command.class)) continue;
            final Command command = method.getAnnotation(Command.class);
            if(command.child()) continue;

            if(base == null)
                base = method;
            else throw new IllegalStateException(String.format("There can only be one base command in %s", name));
        }

        if(base == null)
            throw new IllegalStateException(String.format("There must be exactly one base command in %s", name));

        return base;
    }

    @Nullable
    private static Method findBaseTabComplete(final @NotNull Method[] methods, final @NotNull String name) {
        Method base = null;

        for(final Method method : methods) {
            if(!method.isAnnotationPresent(TabExecute.class)) continue;
            final TabExecute command = method.getAnnotation(TabExecute.class);
            if(command.child()) continue;

            if(base == null)
                base = method;
            else throw new IllegalStateException(String.format("There can only be one base tab complete in %s", name));
        }

        return base;
    }

    private static void validateAnnotations(final @NotNull Method method) {
        if(method.isAnnotationPresent(Command.class) && method.isAnnotationPresent(TabExecute.class))
            throw new IllegalStateException(String.format("Method %s cannot be annotated with both @Command and @TabExecute", method.getName()));
    }

    private static void validateCommandMethod(final @NotNull Method method) {
        validateAnnotations(method);
        if(!method.isAnnotationPresent(Command.class))
            throw new IllegalStateException(String.format("Method %s is not annotated with @Command", method.getName()));
        validateMethodArguments(method);
        if(method.getReturnType() != void.class)
            throw new IllegalStateException(String.format("Method %s must return void", method.getName()));
    }

    private static void validateMethodArguments(final @NotNull Method method) {
        if(method.getParameterCount() != 2)
            throw new IllegalStateException(String.format("Method %s must have exactly two parameters", method.getName()));
        if(!CommandSource.class.isAssignableFrom(method.getParameterTypes()[0]))
            throw new IllegalStateException(String.format("Method %s's first parameter must be a CommandSource", method.getName()));
        if(!CommandArgument.class.isAssignableFrom(method.getParameterTypes()[1]))
            throw new IllegalStateException(String.format("Method %s's second parameter must be a CommandArgument", method.getName()));
    }

    private static void validateTabCompleterMethod(final @NotNull Method method) {
        validateAnnotations(method);
        if(!method.isAnnotationPresent(TabExecute.class))
            throw new IllegalStateException(String.format("Method %s is not annotated with @TabExecute", method.getName()));
        validateMethodArguments(method);
        if(!List.class.isAssignableFrom(method.getReturnType()))
            throw new IllegalStateException(String.format("Method %s must return a List", method.getName()));
    }

    @SuppressWarnings("unchecked")
    private static BiConsumer<CommandSource, CommandArgument> generateCommandMethod(final @NotNull CommandRegistrable instance,
                                                                                    final @NotNull Method method,
                                                                                    final @NotNull String[] names) {
        validateCommandMethod(method);
        final CallSite callSite = LambdaFactory.generate(MethodHandles.lookup(), method);

        try {
            final BiConsumer<CommandSource, CommandArgument> function;
            if(Modifier.isStatic(method.getModifiers()))
                function = (BiConsumer<CommandSource, CommandArgument>) callSite.getTarget().invoke();
            else function = (BiConsumer<CommandSource, CommandArgument>) callSite.getTarget().bindTo(instance).invoke();

            for(final String name : names) {
                if(FUNCTIONS.containsKey(name))
                    ConsoleLogger.warn("Overwriting registered command with name " + name);
                FUNCTIONS.put(name, function);
            }
            return function;
        } catch(final Throwable exception) {
            throw new CommandException("Failed to generate command method.", exception);
        }
    }

    @SuppressWarnings("unchecked")
    private static BiFunction<CommandSource, CommandArgument, List<?>> generateTabCompleter(final @NotNull CommandRegistrable instance,
                                                                                            final @NotNull Method method,
                                                                                            final @NotNull String[] names) {
        validateTabCompleterMethod(method);
        final CallSite callSite = LambdaFactory.generate(MethodHandles.lookup(), method);

        try {
            final BiFunction<CommandSource, CommandArgument, List<?>> function;
            if(Modifier.isStatic(method.getModifiers()))
                function = (BiFunction<CommandSource, CommandArgument, List<?>>) callSite.getTarget().invoke();
            else function = (BiFunction<CommandSource, CommandArgument, List<?>>) callSite.getTarget().bindTo(instance).invoke();

            for(final String name : names) {
                if(TAB_COMPLETES.containsKey(name))
                    ConsoleLogger.warn("Overwriting registered tab executor with name " + name);
                TAB_COMPLETES.put(name, function);
            }
            return function;
        } catch(final Throwable exception) {
            throw new CommandException("Failed to generate tab completer method.", exception);
        }
    }

    @NotNull
    private static CommandExecutor generateCommandExecutor(final @NotNull CommandRegistrable instance, final @NotNull Method method) {
        final Command command = method.getAnnotation(Command.class);
        final SenderType[] senderTypes = command.senders();
        final String senderMessage = command.message();

        final CommandPermission permission = method.getAnnotation(CommandPermission.class);
        final BiConsumer<CommandSource, CommandArgument> function = generateCommandMethod(instance, method, command.value());

        return (sender, name, label, args) -> {
            final CommandSource source = new CommandSource(sender);
            final CommandArgument arguments = new CommandArgument(args);

            if(Arrays.stream(senderTypes).noneMatch(type -> type.getSenderClass().isInstance(sender))) {
                sender.sendMessage(Strings.color(senderMessage));
                return true;
            }

            if(permission != null && Arrays.stream(permission.value()).noneMatch(source::hasPermission)) {
                sender.sendMessage(Strings.color(permission.message()));
                return true;
            }

            function.accept(source, arguments);
            return true;
        };
    }

    @SuppressWarnings("unchecked")
    @NotNull
    private static TabCompleter generateTabCompleter(final @NotNull CommandRegistrable instance, final @NotNull Method method) {
        final TabExecute command = method.getAnnotation(TabExecute.class);
        final CommandPermission permission = method.getAnnotation(CommandPermission.class);
        final BiFunction<CommandSource, CommandArgument, List<?>> function = generateTabCompleter(instance, method, command.value());

        return (sender, name, label, args) -> {
            final CommandSource source = new CommandSource(sender);
            final CommandArgument arguments = new CommandArgument(args);

            if(permission != null && Arrays.stream(permission.value()).noneMatch(source::hasPermission))
                return null;

            return (List<String>) function.apply(source, arguments);
        };
    }

    private static void registerCommand(final @NotNull JavaPlugin plugin, final @NotNull CommandRegistrable registrable,
                                        final @NotNull Method base, final @Nullable Method baseTabComplete,
                                        final @NotNull List<Method> children, final @NotNull List<Method> childTabCompletes) {
        final CommandExecutor baseExecutor = generateCommandExecutor(registrable, base);
        final TabCompleter baseTabCompleter = baseTabComplete == null ? null : generateTabCompleter(registrable, baseTabComplete);
        final Map<String, CommandExecutor> childExecutors = new HashMap<>();
        final Map<String, TabCompleter> childTabCompleters = new HashMap<>();

        for(final Method child : children) {
            final CommandExecutor executor = generateCommandExecutor(registrable, child);
            final String[] names = child.getAnnotation(Command.class).value();
            for(final String name : names)
                childExecutors.put(name, executor);
        }

        for(final Method child : childTabCompletes) {
            final TabCompleter tabCompleter = generateTabCompleter(registrable, child);
            final String[] names = child.getAnnotation(TabExecute.class).value();
            for(final String name : names)
                childTabCompleters.put(name, tabCompleter);
        }

        final CommandExecutor finalExecutor = (sender, name, label, args) -> {
            if(args.length >= 1 && childExecutors.containsKey(args[0])) {
                return childExecutors.get(args[0]).onCommand(sender, name, label, Arrays.copyOfRange(args, 1, args.length));
            }

            return baseExecutor.onCommand(sender, name, label, args);
        };

        final TabCompleter finalTabCompleter = (sender, name, label, args) -> {
            if(args.length >= 1 && childTabCompleters.containsKey(args[0])) {
                return childTabCompleters.get(args[0])
                    .onTabComplete(sender, name, label, Arrays.copyOfRange(args, 1, args.length));
            }

            return baseTabCompleter == null ? null : baseTabCompleter.onTabComplete(sender, name, label, args);
        };

        final Command annotation = base.getAnnotation(Command.class);
        final String[] names = annotation.value();
        for(final String alias : names) {
            final PluginCommand command = plugin.getCommand(alias);
            if(command == null) {
                ConsoleLogger.warn("Failed to register command with name " + alias + " as it does not exist.");
                continue;
            }

            command.setExecutor(finalExecutor);
            command.setTabCompleter(finalTabCompleter);
        }
    }

    /**
     * Attempts to register a registrable with the plugin.
     *
     * @param plugin      The plugin to register the registrable with.
     * @param registrable The registrable to register.
     * @since 2.0
     */
    public static void register(final @NotNull JavaPlugin plugin, final @NotNull CommandRegistrable registrable) {
        final Method[] declaredMethods = registrable.getClass().getDeclaredMethods();
        final Method baseCommand = findBaseCommand(declaredMethods, registrable.getClass().getName());
        final Method baseTabComplete = findBaseTabComplete(declaredMethods, registrable.getClass().getName());
        final List<Method> children = Arrays.stream(declaredMethods)
            .filter(method -> method.isAnnotationPresent(Command.class))
            .filter(method -> method.getAnnotation(Command.class).child())
            .collect(Collectors.toList());
        final List<Method> childTabCompletes = Arrays.stream(declaredMethods)
            .filter(method -> method.isAnnotationPresent(TabExecute.class))
            .filter(method -> method.getAnnotation(TabExecute.class).child())
            .collect(Collectors.toList());

        registerCommand(plugin, registrable, baseCommand, baseTabComplete, children, childTabCompletes);
    }

}
