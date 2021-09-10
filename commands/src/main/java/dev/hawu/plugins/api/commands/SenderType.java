package dev.hawu.plugins.api.commands;

import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents possible types of command senders.
 * @since 1.0
 */
public enum SenderType {

    PLAYER(Player.class),
    BLOCK(BlockCommandSender.class),
    CONSOLE(ConsoleCommandSender.class),
    PROXIED(ProxiedCommandSender.class),
    REMOTE_CONSOLE(RemoteConsoleCommandSender.class),
    ;

    private final Class<? extends CommandSender> senderClass;

    SenderType(@NotNull final Class<? extends CommandSender> cls) {
        this.senderClass = cls;
    }

    /**
     * Gets the underlying {@code Class<? extends CommandSender>} class of the current enum.
     * @return The underlying class.
     * @since 1.0
     */
    @NotNull
    public final Class<? extends CommandSender> getSenderClass() {
        return senderClass;
    }

}
