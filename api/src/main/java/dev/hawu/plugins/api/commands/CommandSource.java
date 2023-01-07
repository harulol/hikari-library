package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.collections.Property;
import dev.hawu.plugins.api.i18n.Locale;
import dev.hawu.plugins.api.user.ExtendedUser;
import dev.hawu.plugins.api.user.UserAdapter;
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

    private final CommandSender base;

    /**
     * Default constructor to construct an instance of
     * {@link CommandSource}.
     *
     * @param base The base sender to wrap.
     * @since 1.0
     */
    public CommandSource(@NotNull final CommandSender base) {
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
     * @return A property containing the player if the cast was successful,
     * @since 2.0
     */
    @NotNull
    public Property<Player> getPlayer() {
        return this.base instanceof Player ? Property.of((Player) this.base) : Property.empty();
    }

    /**
     * Attempts to cast the sender to a {@link Player} and returns
     * its instance of {@link ExtendedUser}.
     *
     * @return A property containing the extended user if the cast was successful,
     * @since 2.0
     */
    @NotNull
    public Property<ExtendedUser> getUser() {
        return getPlayer().map(UserAdapter.getAdapter()::getUser);
    }

    /**
     * Attempts to cast the base sender to a {@link ProxiedCommandSender}.
     *
     * @return A property containing the proxied sender if the cast was successful,
     * @since 2.0
     */
    @NotNull
    public Property<ProxiedCommandSender> getProxied() {
        return this.base instanceof ProxiedCommandSender ? Property.of((ProxiedCommandSender) this.base) : Property.empty();
    }

    /**
     * Attempts to cast the base sender to a {@link ConsoleCommandSender}.
     *
     * @return A property containing the console sender if the cast was successful,
     * @since 2.0
     */
    @NotNull
    public Property<ConsoleCommandSender> getConsole() {
        return this.base instanceof ConsoleCommandSender ? Property.of((ConsoleCommandSender) this.base) : Property.empty();
    }

    /**
     * Attempts to cast the base sender to a {@link BlockCommandSender}.
     *
     * @return A property containing the block sender if the cast was successful,
     * @since 2.0
     */
    @NotNull
    public Property<BlockCommandSender> getBlock() {
        return this.base instanceof BlockCommandSender ? Property.of((BlockCommandSender) this.base) : Property.empty();
    }

    /**
     * Attempts to cast the base sender to a {@link ProxiedCommandSender} then
     * retrieve the {@link ProxiedCommandSender#getCaller()} property from the instance.
     *
     * @return A property containing the caller of the proxied sender.
     * @since 2.0
     */
    @NotNull
    public Property<CommandSender> getCaller() {
        return getProxied().map(ProxiedCommandSender::getCaller);
    }

    /**
     * Attempts to cast the base sender to a {@link ProxiedCommandSender} then
     * retrieve the {@link ProxiedCommandSender#getCallee()} property from the instance.
     *
     * @return A property containing the callee of the proxied sender.
     * @since 2.0
     */
    @NotNull
    public Property<CommandSender> getCallee() {
        return getProxied().map(ProxiedCommandSender::getCallee);
    }

    /**
     * Attempts to retrieve the locale of a CommandSender.
     * <p>
     * Will fail if the locale could not be retrieved from a player.
     *
     * @return A non-null {@link Locale} instance.
     * @since 2.0
     */
    @NotNull
    public Locale getLocale() {
        return UserAdapter.getAdapter().getUser((Player) this.base).getLocale();
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
