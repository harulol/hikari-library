package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.misc.ConsoleLogger;
import dev.hawu.plugins.api.reflect.LambdaFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * A registry to register commands using
 * annotations syntax.
 *
 * @since 2.0
 */
public final class CommandRegistry {

    private static final Map<JavaPlugin, CommandRegistry> REGISTRIES = new HashMap<>();

    private final Map<String, ExtendedCommandExecutor> COMMAND_EXECUTOR_MAP;
    private final Map<String, ExtendedTabCompleter> TAB_COMPLETER_MAP;
    private final Set<String> CHECKED_COMMAND_EXECUTORS;
    private final Set<String> CHECKED_TAB_COMPLETERS;

    private CommandRegistry() {
        COMMAND_EXECUTOR_MAP = new HashMap<>();
        TAB_COMPLETER_MAP = new HashMap<>();
        CHECKED_COMMAND_EXECUTORS = new HashSet<>();
        CHECKED_TAB_COMPLETERS = new HashSet<>();
    }

    /**
     * Attempts to register all annotated methods as command executors
     * in a given class.
     * <p>
     * Some methods with low visibility (private, package-protected) might not be optimized
     * as well as those public.
     * <p>
     * For best performance, make sure that the methods are public or provide your
     * own version of {@link MethodHandles#lookup()} in {@link CommandRegistry#register(Lookup, JavaPlugin, CommandRegistrable...)}
     *
     * @param plugin       The plugin that is registering the commands.
     * @param registrables The class(es) that contains the methods to be registered.
     */
    public static void register(final @NotNull JavaPlugin plugin, final @NotNull CommandRegistrable... registrables) {
        register(MethodHandles.lookup(), plugin, registrables);
    }

    /**
     * Attempts to register all annotated methods as command executors
     * in a given class.
     * <p>
     * You should provide your own lookup instance by invoking {@link MethodHandles#lookup()}
     * if you have executors as private methods to reduce overhead for Reflections.
     *
     * @param lookup       The lookup of the class, manually provided.
     * @param plugin       The plugin instance.
     * @param registrables The class(es) to register.
     * @since 2.0
     */
    public static void register(final @NotNull MethodHandles.Lookup lookup,
                                final @NotNull JavaPlugin plugin, final @NotNull CommandRegistrable... registrables) {
        REGISTRIES.putIfAbsent(plugin, new CommandRegistry());
        final CommandRegistry registry = REGISTRIES.get(plugin);

        for(final CommandRegistrable registrable : registrables) {
            final List<Method> commandMethods = new ArrayList<>();
            final List<Method> tabMethods = new ArrayList<>();

            for(final Method method : registrable.getClass().getDeclaredMethods()) {
                if(registry.checkCommandMethod(plugin, method))
                    commandMethods.add(method);
                else if(registry.checkTabMethod(plugin, method))
                    tabMethods.add(method);
            }

            commandMethods.forEach(method -> registry.registerCommandMethod(lookup, registrable, plugin, method));
            tabMethods.forEach(method -> registry.registerTabCompleter(lookup, registrable, plugin, method));
        }

        registry.bindChildMethods(plugin);
    }

    /**
     * Clears all registries.
     * <p>
     * Should not be called by implementations.
     *
     * @since 2.0
     */
    public static void unregisterAll() {
        REGISTRIES.forEach((plugin, registry) -> {
            registry.CHECKED_TAB_COMPLETERS.clear();
            registry.CHECKED_COMMAND_EXECUTORS.clear();
            registry.TAB_COMPLETER_MAP.clear();
            registry.COMMAND_EXECUTOR_MAP.clear();
        });
        REGISTRIES.clear();
    }

    private String standardize(final String command) {
        return command.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    private void checkVisibility(final JavaPlugin plugin, final Method method) {
        if(!Modifier.isPublic(method.getModifiers()))
            ConsoleLogger.warnf(plugin, "Command/Tab method %s should be public if possible.", method.getName());
    }

    private boolean checkBadReturnType(final JavaPlugin plugin, final Method method, final Class<?> cls) {
        if(method.getReturnType() != cls) {
            ConsoleLogger.warnf(plugin, "Command/Tab method %s should return %s.", method.getName(), cls.getSimpleName());
            return true;
        }
        return false;
    }

    private boolean checkParameters(final JavaPlugin plugin, final Method method) {
        final Class<?>[] params = method.getParameterTypes();
        if(params.length != 2) {
            ConsoleLogger.warnf(plugin, "Command method %s should have 2 parameters.", method.getName());
            return false;
        }
        if(params[0] != CommandSource.class) {
            ConsoleLogger.warnf(plugin, "Command method %s should have CommandSource as the first parameter.", method.getName());
            return false;
        }
        if(params[1] != CommandArgument.class) {
            ConsoleLogger.warnf(plugin, "Command method %s should have CommandArgument as the second parameter.", method.getName());
            return false;
        }
        return true;
    }

    private boolean checkCommandMethod(final @NotNull JavaPlugin plugin, final @NotNull Method method) {
        if(!method.isAnnotationPresent(Command.class) || method.isAnnotationPresent(TabExecute.class))
            return false;

        checkVisibility(plugin, method);
        if(checkBadReturnType(plugin, method, void.class)) return false;
        return checkParameters(plugin, method);
    }

    private boolean checkTabMethod(final @NotNull JavaPlugin plugin, final @NotNull Method method) {
        if(!method.isAnnotationPresent(TabExecute.class) || method.isAnnotationPresent(Command.class))
            return false;

        checkVisibility(plugin, method);
        if(checkBadReturnType(plugin, method, List.class)) return false;
        return checkParameters(plugin, method);
    }

    /**
     * Attempts to generate a functional lambda command executor for the provided method
     * in the registrable instance.
     * <p>
     * Will fallback on {@link Method#invoke(Object, Object...)} if the lambda could not
     * be generated for any reason.
     *
     * @param lookup   The lookup in case of private methods.
     * @param instance The instance to register with.
     * @param method   The method to seed.
     * @return The generated BiConsumer for command executions.
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    private BiConsumer<CommandSource, CommandArgument> generateFunctionalCommand(
        final @NotNull MethodHandles.Lookup lookup,
        final @NotNull CommandRegistrable instance,
        final @NotNull JavaPlugin plugin,
        final @NotNull Method method
    ) {
        method.setAccessible(true);

        try {
            final CallSite site = LambdaFactory.generate(lookup, method);
            if(Modifier.isStatic(method.getModifiers()))
                return (BiConsumer<CommandSource, CommandArgument>) site.getTarget().invoke();
            else
                return (BiConsumer<CommandSource, CommandArgument>) site.getTarget().bindTo(instance).invoke();
        } catch(final Throwable exception) {
            ConsoleLogger.warnf(plugin, "Failed to generate lambda for command %s in %s. Using Reflections as fallback",
                method.getName(), method.getDeclaringClass().getName());

            return (sender, args) -> {
                try {
                    if(Modifier.isStatic(method.getModifiers()))
                        method.invoke(null, sender, args);
                    else method.invoke(instance, sender, args);
                } catch(final Exception cmdException) {
                    cmdException.printStackTrace();
                }
            };
        }
    }

    /**
     * Attempts to generate a functional lambda tab completer for the provided method
     * in the registrable instance.
     * <p>
     * Will fallback on {@link Method#invoke(Object, Object...)} if the lambda could not
     * be generated for any reason.
     *
     * @param lookup   The lookup in case of private methods.
     * @param instance The instance to register with.
     * @param method   The method to seed.
     * @return The generated BiConsumer for command executions.
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    private BiFunction<CommandSource, CommandArgument, List<String>> generateFunctionalTab(
        final @NotNull MethodHandles.Lookup lookup,
        final @NotNull CommandRegistrable instance,
        final @NotNull JavaPlugin plugin,
        final @NotNull Method method
    ) {
        method.setAccessible(true);

        try {
            final CallSite site = LambdaFactory.generate(lookup, method);
            if(Modifier.isStatic(method.getModifiers()))
                return (BiFunction<CommandSource, CommandArgument, List<String>>) site.getTarget().invoke();
            else
                return (BiFunction<CommandSource, CommandArgument, List<String>>) site.getTarget().bindTo(instance).invoke();
        } catch(final Throwable exception) {
            ConsoleLogger.warnf(plugin, "Failed to generate lambda for tab %s in %s. Using Reflections as fallback",
                method.getName(), method.getDeclaringClass().getName());

            return (sender, args) -> {
                try {
                    if(Modifier.isStatic(method.getModifiers()))
                        return (List<String>) method.invoke(null, sender, args);
                    else return (List<String>) method.invoke(instance, sender, args);
                } catch(final Exception cmdException) {
                    cmdException.printStackTrace();
                }
                return Collections.emptyList();
            };
        }
    }

    // It is guaranteed that the method passed in has to have a @Command annotation.
    private void registerCommandMethod(
        final @NotNull MethodHandles.Lookup lookup,
        final @NotNull CommandRegistrable registrable,
        final @NotNull JavaPlugin plugin,
        final @NotNull Method method
    ) {
        final Command command = method.getAnnotation(Command.class);
        final CommandPermission permission = method.getAnnotation(CommandPermission.class);
        final BiConsumer<CommandSource, CommandArgument> consumer = generateFunctionalCommand(lookup, registrable, plugin, method);

        final List<String> commandNames = new ArrayList<>();
        commandNames.add(standardize(command.value()));
        Arrays.stream(command.aliases())
            .map(this::standardize)
            .map(alias -> commandNames.get(0).substring(0, commandNames.get(0).lastIndexOf(' ') + 1) + alias)
            .forEach(commandNames::add);

        for(final String commandName : commandNames) {
            if(COMMAND_EXECUTOR_MAP.containsKey(commandName))
                ConsoleLogger.warnf(plugin, "Command \"%s\" is already registered. It will be overwritten.", commandName);
            COMMAND_EXECUTOR_MAP.put(commandName, CommandExecutorFactory.generateCommandExecutor(command, permission, consumer));
        }
    }

    private void registerTabCompleter(
        final @NotNull MethodHandles.Lookup lookup,
        final @NotNull CommandRegistrable registrable,
        final @NotNull JavaPlugin plugin,
        final @NotNull Method method
    ) {
        final TabExecute tabExecute = method.getAnnotation(TabExecute.class);
        final CommandPermission permission = method.getAnnotation(CommandPermission.class);
        final BiFunction<CommandSource, CommandArgument, List<String>> function = generateFunctionalTab(lookup, registrable, plugin, method);

        final List<String> commandNames = new ArrayList<>();
        commandNames.add(standardize(tabExecute.value()));
        Arrays.stream(tabExecute.aliases())
            .map(this::standardize)
            .map(alias -> commandNames.get(0).substring(0, commandNames.get(0).lastIndexOf(' ') + 1) + alias)
            .forEach(commandNames::add);

        for(final String commandName : commandNames) {
            if(TAB_COMPLETER_MAP.containsKey(commandName))
                ConsoleLogger.warnf(plugin, "Tab completer for \"%s\" is already registered. It will be overwritten.", commandName);
            TAB_COMPLETER_MAP.put(commandName, CommandExecutorFactory.generateTabCompleter(tabExecute, permission, function));
        }
    }

    private void bindChildMethods(final @NotNull JavaPlugin plugin) {
        for(Entry<String, ExtendedCommandExecutor> entry : COMMAND_EXECUTOR_MAP.entrySet()) {
            if(CHECKED_COMMAND_EXECUTORS.contains(entry.getKey()))
                continue;

            final String[] commandParts = entry.getKey().split(" ");
            if(commandParts.length < 1)
                continue;
            if(commandParts.length == 1) {
                if(plugin.getCommand(commandParts[0]) == null)
                    ConsoleLogger.severef(plugin, "Command \"%s\" is not defined in plugin.yml. Ignoring...", commandParts[0]);
                else plugin.getCommand(commandParts[0]).setExecutor(entry.getValue());
                continue;
            }

            final String parentCommand = String.join(" ", Arrays.copyOfRange(commandParts, 0, commandParts.length - 1));
            final String childCommand = commandParts[commandParts.length - 1];
            if(!COMMAND_EXECUTOR_MAP.containsKey(parentCommand)) {
                ConsoleLogger.warnf(plugin, "Parent command \"%s\" for child command \"%s\" does not exist.", parentCommand, entry.getKey());
                continue;
            }

            final ExtendedCommandExecutor parentExecutor = COMMAND_EXECUTOR_MAP.get(parentCommand);
            if(parentExecutor.subCommands.containsKey(childCommand))
                ConsoleLogger.warnf(plugin, "Child command \"%s\" is already registered. It will be overwritten.", entry.getKey());

            parentExecutor.subCommands.put(childCommand, entry.getValue());
            CHECKED_COMMAND_EXECUTORS.add(entry.getKey());
        }

        for(Entry<String, ExtendedTabCompleter> entry : TAB_COMPLETER_MAP.entrySet()) {
            if(CHECKED_TAB_COMPLETERS.contains(entry.getKey()))
                continue;

            final String[] commandParts = entry.getKey().split(" ");
            if(commandParts.length < 1)
                continue;
            if(commandParts.length == 1) {
                if(plugin.getCommand(commandParts[0]) == null)
                    ConsoleLogger.severef(plugin, "Command \"%s\" is not defined in plugin.yml. Ignoring...", commandParts[0]);
                else plugin.getCommand(commandParts[0]).setTabCompleter(entry.getValue());
                continue;
            }

            final String parentCommand = String.join(" ", Arrays.copyOfRange(commandParts, 0, commandParts.length - 1));
            final String childCommand = commandParts[commandParts.length - 1];
            if(!TAB_COMPLETER_MAP.containsKey(parentCommand)) {
                ConsoleLogger.warnf(plugin, "Parent tab \"%s\" for child tab \"%s\" does not exist.", parentCommand, entry.getKey());
                continue;
            }

            final ExtendedTabCompleter parentCompleter = TAB_COMPLETER_MAP.get(parentCommand);
            if(parentCompleter.subCompleters.containsKey(childCommand))
                ConsoleLogger.warnf(plugin, "Child tab \"%s\" is already registered. It will be overwritten.", entry.getKey());

            parentCompleter.subCompleters.put(childCommand, entry.getValue());
            CHECKED_TAB_COMPLETERS.add(entry.getKey());
        }
    }

}
