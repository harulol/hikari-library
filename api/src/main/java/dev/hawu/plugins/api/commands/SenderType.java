package dev.hawu.plugins.api.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents possible types of command senders.
 *
 * @since 1.0
 */
public enum SenderType {

    /**
     * Represents a player that sends a command.
     *
     * @since 1.0
     */
    PLAYER(Player.class),

    /**
     * Represents a block that can send a command,
     * such as command blocks.
     *
     * @since 1.0
     */
    BLOCK(BlockCommandSender.class),

    /**
     * Represents the console that can send a command.
     *
     * @since 1.0
     */
    CONSOLE(ConsoleCommandSender.class),

    /**
     * Represents the type of sender that was proxied via
     * {@code /execute} or anything similar.
     *
     * @since 1.0
     */
    PROXIED(ProxiedCommandSender.class),

    /**
     * Represents the remote console that can send a command.
     *
     * @since 1.0
     */
    REMOTE_CONSOLE(RemoteConsoleCommandSender.class),
    ;

    private final Class<? extends CommandSender> senderClass;

    SenderType(@NotNull final Class<? extends CommandSender> cls) {
        this.senderClass = cls;
    }

    /**
     * Gets the underlying {@code Class<? extends CommandSender>} class of the current enum.
     *
     * @return The underlying class.
     * @since 1.0
     */
    @NotNull
    public final Class<? extends CommandSender> getSenderClass() {
        return senderClass;
    }

}
