package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.collections.tuples.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Implementation of {@link TabExecutor} that allows for cleaner
 * writing but not extremely bloated with methods and configurations.
 *
 * @since 1.0
 */
public abstract class AbstractCommandClass implements TabExecutor {

    private final Set<@NotNull SenderType> senders = new HashSet<>();
    private final Map<@NotNull String, @NotNull AbstractCommandClass> subcommands = new HashMap<>();
    private final Set<@NotNull String> aliases = new HashSet<>();
    private final @NotNull String name;

    private @Nullable String permission = null;
    private @NotNull String permissionMessage = "&cI'm sorry, but you do not have permission to perform this command.";
    private @NotNull String badSenderMessage = "&cThis command was not tailored for your type of sender.";

    /**
     * Main constructor that simply initializes the class with the name
     * of the command.
     *
     * @param name The name to initialize with.
     * @since 1.0
     */
    public AbstractCommandClass(@NotNull final String name) {
        this.name = name;
    }

    /**
     * Secondary constructor that lets the child class inherits
     * the permission, permission message and the bad sender message
     * from the parent class.
     *
     * @param name   The name to initialize with.
     * @param parent The parent to inherit from.
     * @since 1.0
     */
    public AbstractCommandClass(@NotNull final String name, @NotNull final AbstractCommandClass parent) {
        this.name = name;
        this.permission = parent.permission;
        this.permissionMessage = parent.permissionMessage;
        this.badSenderMessage = parent.badSenderMessage;
    }

    /**
     * Handles the tab completions for this command.
     *
     * @param sender The command's sender.
     * @param args   The command's arguments.
     * @return The tab completion results at the moment.
     * @since 1.0
     */
    @Nullable
    public List<String> tab(@NotNull final CommandSource sender, @NotNull final CommandArgument args) {
        return null;
    }

    /**
     * Handles the command executions for this command.
     *
     * @param sender The command's sender.
     * @param args   The command's arguments.
     * @since 1.0
     */
    public abstract void run(@NotNull final CommandSource sender, @NotNull final CommandArgument args);

    /**
     * Allows players to use this command.
     *
     * @since 1.0
     */
    protected final void allowPlayers() {
        senders.add(SenderType.PLAYER);
    }

    /**
     * Allows the console to use this command.
     *
     * @since 1.0
     */
    protected final void allowConsole() {
        senders.add(SenderType.CONSOLE);
    }

    /**
     * Allows block senders to use this command.
     *
     * @since 1.0
     */
    protected final void allowBlocks() {
        senders.add(SenderType.BLOCK);
    }

    /**
     * Allows proxied senders to use this command.
     *
     * @since 1.0
     */
    protected final void allowProxieds() {
        senders.add(SenderType.PROXIED);
    }

    /**
     * Allows proxied consoles to use this command.
     *
     * @since 1.0
     */
    protected final void allowRemoteConsole() {
        senders.add(SenderType.REMOTE_CONSOLE);
    }

    /**
     * Allows any types of sender to use this command.
     *
     * @since 1.0
     */
    protected final void allowAny() {
        senders.addAll(Arrays.asList(SenderType.BLOCK, SenderType.CONSOLE, SenderType.PLAYER, SenderType.PROXIED, SenderType.REMOTE_CONSOLE));
    }

    /**
     * Retrieves the permission of this command.
     *
     * @return The permission of this command.
     * @since 1.0
     */
    @Nullable
    public String getPermission() {
        return permission;
    }

    /**
     * Overrides the value of this command's permission.
     *
     * @param permission The permission to override with.
     * @since 1.0
     */
    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }

    /**
     * Retrieves the message that gets sent to users when they
     * are not authorized with the permission.
     *
     * @return The permission message.
     * @since 1.0
     */
    public @NotNull
    String getPermissionMessage() {
        return permissionMessage;
    }

    /**
     * Overrides the value of this command's permission message.
     *
     * @param permissionMessage The message to override with.
     * @since 1.0
     */
    public void setPermissionMessage(@NotNull String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    /**
     * Retrieves the message that gets sent to senders if their
     * types are not accounted for in this command.
     *
     * @return The bad sender message.
     * @since 1.0
     */
    public @NotNull
    String getBadSenderMessage() {
        return badSenderMessage;
    }

    /**
     * Overrides the value of this command's bad sender message.
     *
     * @param badSenderMessage The message to override with.
     * @since 1.0
     */
    public void setBadSenderMessage(@NotNull String badSenderMessage) {
        this.badSenderMessage = badSenderMessage;
    }

    /**
     * Configures the aliases for the command. This does not have any effect
     * if this is a root command.
     *
     * @param aliases The aliases of this command.
     * @return The same receiver.
     * @since 1.0
     */
    protected final AbstractCommandClass alias(@NotNull final String @NotNull ... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    /**
     * Binds a sub-command to this class as parent.
     *
     * @param cls The sub-command class.
     * @since 1.0
     */
    protected final void bind(@NotNull final AbstractCommandClass cls) {
        bind(cls.name, cls);
    }

    /**
     * Binds a sub-command to this class as parent, with uses
     * the specified alias as name.
     * <p>
     * This method does not use the name field to initialize with,
     * unlike {@link AbstractCommandClass#bind(AbstractCommandClass)}.
     *
     * @param alias The alias for this command.
     * @param cls   The sub-command class.
     * @since 1.1
     */
    protected final void bind(@NotNull final String alias, @NotNull final AbstractCommandClass cls) {
        cls.aliases.forEach(s -> subcommands.put(s.toLowerCase(), cls));
        subcommands.put(alias, cls);
    }

    /**
     * Registers this command as a root command.
     *
     * @param plugin The plugin to register with.
     * @since 1.0
     */
    protected final void register(@NotNull final JavaPlugin plugin) {
        plugin.getCommand(name).setExecutor(this);
        plugin.getCommand(name).setTabCompleter(this);
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final CommandSource source = new CommandSource(sender);
        final CommandArgument argument = new CommandArgument(args);
        if(args.length > 0 && subcommands.containsKey(args[0].toLowerCase())) {
            subcommands.get(args[0].toLowerCase()).onCommand(sender, command, label, argument.sliceArray(1, args.length));
        } else if(permission != null && !source.isAuthorized(permission)) {
            source.sendMessage(Strings.fillPlaceholders(permissionMessage, new Pair<>("perm", permission)));
        } else if(senders.stream().noneMatch(s -> s.getSenderClass().isInstance(sender))) {
            source.sendMessage(badSenderMessage);
        } else run(source, argument);
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final CommandArgument argument = new CommandArgument(args);
        if(args.length > 0 && subcommands.containsKey(args[0].toLowerCase())) {
            return subcommands.get(args[0].toLowerCase()).onTabComplete(sender, command, alias, argument.sliceArray(1, args.length));
        } else if((permission != null && !sender.hasPermission(permission)) || senders.stream().noneMatch(s -> s.getSenderClass().isInstance(sender))) {
            return null;
        } else return tab(new CommandSource(sender), argument);
    }

}
