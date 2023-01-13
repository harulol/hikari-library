package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.ObjectUtils;
import dev.hawu.plugins.api.Strings;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Factory to generate executors as wrappers for commands.
 *
 * @since 2.0
 */
final class CommandExecutorFactory {

    /**
     * Attempts to generate a {@link ExtendedCommandExecutor} with proper child handling.
     *
     * @param command    The command object.
     * @param permission The permission of the command.
     * @param consumer   The consumer to execute.
     * @return The generated executor.
     * @since 2.0
     */
    public static ExtendedCommandExecutor generateCommandExecutor(
        final @NotNull Command command,
        final @Nullable CommandPermission permission,
        final @NotNull BiConsumer<CommandSource, CommandArgument> consumer
    ) {
        return new ExtendedCommandExecutor() {
            @Override
            public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command cmd, final String label, final String[] args) {
                final CommandSource source = new CommandSource(sender);
                final CommandArgument arguments = new CommandArgument(args);

                if(args.length >= 1 && this.subCommands.containsKey(args[0]))
                    return this.subCommands.get(args[0]).onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));

                if(Arrays.stream(command.senders()).noneMatch(type -> type.getSenderClass().isInstance(sender))) {
                    sender.sendMessage(Strings.color(command.message()));
                    return true;
                }

                if(permission != null && Arrays.stream(permission.value()).noneMatch(source::isAuthorized)) {
                    sender.sendMessage(Strings.color(permission.message()));
                    return true;
                }

                consumer.accept(source, arguments);
                return true;
            }
        };
    }

    /**
     * Attempts to generate a {@link ExtendedTabCompleter} with proper child handling.
     *
     * @param execute    The tab object.
     * @param permission The permission of the command.
     * @param function   The function to invoke.
     * @return The generated executor.
     * @since 2.0
     */
    public static ExtendedTabCompleter generateTabCompleter(
        final @NotNull TabExecute execute,
        final @Nullable CommandPermission permission,
        final @NotNull BiFunction<CommandSource, CommandArgument, List<String>> function
    ) {
        return new ExtendedTabCompleter() {
            @Override
            public List<String> onTabComplete(final CommandSender sender, final org.bukkit.command.Command cmd, final String label, final String[] args) {
                final CommandSource source = new CommandSource(sender);
                final CommandArgument arguments = new CommandArgument(args);

                if(args.length >= 1 && this.subCompleters.containsKey(args[0]))
                    return this.subCompleters.get(args[0]).onTabComplete(sender, cmd, label,
                        Arrays.copyOfRange(args, 1, args.length));

                if(Arrays.stream(execute.senders()).noneMatch(type -> type.getSenderClass().isInstance(sender))
                    || (permission != null && Arrays.stream(permission.value()).noneMatch(source::isAuthorized)))
                    return null;

                final String lastArgument = ObjectUtils.elvis(arguments.lastOrNull(), "").toLowerCase();
                return execute.autoFilter()
                    ? function.apply(source, arguments)
                    .stream()
                    .filter(s -> s.toLowerCase().startsWith(lastArgument))
                    .collect(Collectors.toList())
                    : function.apply(source, arguments);
            }
        };
    }

}
