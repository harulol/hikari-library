package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.Strings;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A wrapper class that wraps an instance of {@link CommandSender}
 * to provide quick casting and sending colored messages.
 * <p>
 * This represents the source of a command.
 *
 * @since 1.0
 */
public final class CommandSource implements CommandSender {

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
    @Deprecated
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

    /**
     * Returns the server instance that this command is running on
     *
     * @return Server instance
     * @since 1.1
     */
    @Override
    @NotNull
    public Server getServer() {
        return base.getServer();
    }

    /**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     * @since 1.1
     */
    @Override
    @NotNull
    public String getName() {
        return base.getName();
    }

    /**
     * Checks if this object contains an override for the specified
     * permission, by fully qualified name
     *
     * @param name Name of the permission
     * @return True if the permission is set, false otherwise.
     * @since 1.1
     */
    @Override
    public boolean isPermissionSet(final @NotNull String name) {
        return base.isPermissionSet(name);
    }

    /**
     * Checks if this object contains an override for the specified {@link Permission}
     *
     * @param perm Permission to check
     * @return True if the permission is set, false otherwise
     * @since 1.1
     */
    @Override
    public boolean isPermissionSet(final @NotNull Permission perm) {
        return base.isPermissionSet(perm);
    }

    /**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned.
     *
     * @param name Name of the permission
     * @return Value of the permission
     * @since 1.1
     */
    @Override
    public boolean hasPermission(final @NotNull String name) {
        return base.hasPermission(name);
    }

    /**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned
     *
     * @param perm Permission to get
     * @return Value of the permission
     * @since 1.1
     */
    @Override
    public boolean hasPermission(final @NotNull Permission perm) {
        return base.hasPermission(perm);
    }

    /**
     * Adds a new {@link PermissionAttachment} with a single permission by
     * name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param name   Name of the permission to attach
     * @param value  Value of the permission
     * @return The PermissionAttachment that was just created
     * @since 1.1
     */
    @Override
    @NotNull
    public PermissionAttachment addAttachment(final @NotNull Plugin plugin, final @NotNull String name, final boolean value) {
        return base.addAttachment(plugin, name, value);
    }

    /**
     * Adds a new empty {@link PermissionAttachment} to this object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @return The PermissionAttachment that was just created
     * @since 1.1
     */
    @Override
    @NotNull
    public PermissionAttachment addAttachment(final @NotNull Plugin plugin) {
        return base.addAttachment(plugin);
    }

    /**
     * Temporarily adds a new {@link PermissionAttachment} with a single
     * permission by name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param name   Name of the permission to attach
     * @param value  Value of the permission
     * @param ticks  Amount of ticks to automatically remove this attachment
     *               after
     * @return The PermissionAttachment that was just created
     * @since 1.1
     */
    @Override
    @NotNull
    public PermissionAttachment addAttachment(final @NotNull Plugin plugin, final @NotNull String name, final boolean value, final int ticks) {
        return base.addAttachment(plugin, name, value, ticks);
    }

    /**
     * Temporarily adds a new empty {@link PermissionAttachment} to this
     * object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *               or disabled
     * @param ticks  Amount of ticks to automatically remove this attachment
     *               after
     * @return The PermissionAttachment that was just created
     * @since 1.1
     */
    @Override
    @Nullable
    public PermissionAttachment addAttachment(final @NotNull Plugin plugin, final int ticks) {
        return base.addAttachment(plugin, ticks);
    }

    /**
     * Removes the given {@link PermissionAttachment} from this object
     *
     * @param attachment Attachment to remove
     * @throws IllegalArgumentException Thrown when the specified attachment
     *                                  isn't part of this object
     * @since 1.1
     */
    @Override
    public void removeAttachment(final @NotNull PermissionAttachment attachment) {
        base.removeAttachment(attachment);
    }

    /**
     * Recalculates the permissions for this object, if the attachments have
     * changed values.
     * <p>
     * This should very rarely need to be called from a plugin.
     *
     * @since 1.1
     */
    @Override
    public void recalculatePermissions() {
        base.recalculatePermissions();
    }

    /**
     * Gets a set containing all of the permissions currently in effect by
     * this object
     *
     * @return Set of currently effective permissions
     * @since 1.1
     */
    @Override
    @NotNull
    public Set<@NotNull PermissionAttachmentInfo> getEffectivePermissions() {
        return base.getEffectivePermissions();
    }

    /**
     * Checks if this object is a server operator
     *
     * @return True if this is an operator, false otherwise
     * @since 1.1
     */
    @Override
    public boolean isOp() {
        return base.isOp();
    }

    /**
     * Sets the operator status of this object
     *
     * @param value New operator value
     * @since 1.1
     */
    @Override
    public void setOp(final boolean value) {
        base.setOp(value);
    }
}
