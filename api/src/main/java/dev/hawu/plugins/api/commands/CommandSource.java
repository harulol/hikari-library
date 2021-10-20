package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.Strings;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wrapper class that wraps an instance of {@link CommandSender}
 * to provide quick casting and sending colored messages.
 * <p>
 * This represents the source of a command.
 *
 * @since 1.0
 */
public final class CommandSource {

    @NotNull
    private final CommandSender base;

    /**
     * Default constructor to construct an instance of
     * {@link CommandSource}.
     *
     * @param base The base sender to wrap.
     * @since 1.0
     */
    CommandSource(@NotNull final CommandSender base) {
        this.base = base;
    }

    /**
     * Gets the underlying sender base of the command source.
     *
     * @return A non-null {@link CommandSender}.
     * @since 1.1
     */
    @NotNull
    public CommandSender getBase() {
        return base;
    }

    /**
     * Attempts to cast the base sender to a {@link Player}.
     *
     * @return A non-null {@link Player} instance if it can be casted.
     * @throws ClassCastException If the cast did not succeed.
     * @since 1.0
     */
    @NotNull
    public Player getPlayer() {
        return (Player) base;
    }

    /**
     * Attempts to cast the base sender to a {@link ProxiedCommandSender}.
     *
     * @return A non-null {@link ProxiedCommandSender} instance after casting.
     * @throws ClassCastException If the cast did not succeed.
     * @since 1.0
     */
    @NotNull
    public ProxiedCommandSender getProxied() {
        return (ProxiedCommandSender) base;
    }

    /**
     * Attempts to cast the base sender to a {@link ConsoleCommandSender}.
     *
     * @return A non-null {@link ConsoleCommandSender} instance after casting.
     * @throws ClassCastException If the cast did not succeed.
     * @since 1.0
     */
    @NotNull
    public ConsoleCommandSender getConsole() {
        return (ConsoleCommandSender) base;
    }

    /**
     * Attempts to cast the base sender to a {@link BlockCommandSender}.
     *
     * @return A non-null {@link BlockCommandSender} instance after casting.
     * @throws ClassCastException If the cast did not succeed.
     * @since 1.0
     */
    @NotNull
    public BlockCommandSender getBlock() {
        return (BlockCommandSender) base;
    }

    /**
     * Attempts to cast the base sender to a {@link ProxiedCommandSender} then
     * retrieve the {@link ProxiedCommandSender#getCaller()} property from the instance.
     *
     * @return A non-null {@link CommandSender} instance after casting.
     * @throws ClassCastException If the cast to {@link ProxiedCommandSender} did not succeed.
     * @since 1.0
     */
    @NotNull
    public CommandSender getCaller() {
        return getProxied().getCaller();
    }

    /**
     * Attempts to cast the base sender to a {@link ProxiedCommandSender} then
     * retrieve the {@link ProxiedCommandSender#getCallee()} property from the instance.
     *
     * @return A non-null {@link CommandSender} instance after casting.
     * @throws ClassCastException If the cast to {@link ProxiedCommandSender} did not succeed.
     * @since 1.0
     */
    @NotNull
    public CommandSender getCallee() {
        return getProxied().getCallee();
    }

    /**
     * Delegates the permission checking to the base sender and retrieves
     * the result of the check.
     *
     * @param permission The permission to check with.
     * @return True if the sender does have the permission, false otherwise.
     * @since 1.0
     */
    public boolean isAuthorized(@NotNull final String permission) {
        return base.hasPermission(permission);
    }

    /**
     * Delegates the permission checking to the base sender and retrieves
     * the result of the check.
     *
     * @param permission The permission to check with.
     * @return True if the sender does have the permission, false otherwise.
     * @since 1.0
     */
    public boolean isAuthorized(@NotNull final Permission permission) {
        return base.hasPermission(permission);
    }

    /**
     * Sends a colored message to the sender.
     * If the passed in parameter is {@code null}, nothing will be sent.
     *
     * @param message The message to send after coloring.
     * @since 1.0
     */
    public void sendMessage(@Nullable final String message) {
        if(message == null) return;
        base.sendMessage(Strings.color(message));
    }

    /**
     * Sends a bunch of colored messages to the sender.
     * If the passed in parameter is {@code null}, nothing will be sent.
     * <p>
     * If an element in the array is {@code null}, that element is silently ignored.
     *
     * @param messages The messages to send after coloring.
     * @since 1.0
     */
    public void sendMessage(@Nullable final String @Nullable [] messages) {
        if(messages == null) return;
        for(final String s : messages) {
            if(s != null) base.sendMessage(Strings.color(s));
        }
    }

}
