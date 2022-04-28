package dev.hawu.plugins.api.impl;

import dev.hawu.plugins.api.collections.Property;
import dev.hawu.plugins.api.user.ExtendedUser;
import dev.hawu.plugins.api.user.UserAdapter;
import dev.hawu.plugins.hikarilibrary.CraftUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * The implementation for the user adapter.
 *
 * @since 1.6
 */
public final class UserAdapterImpl extends UserAdapter {

    private static final UserAdapterImpl ADAPTER = new UserAdapterImpl();
    private static final Map<UUID, ExtendedUser> users = new HashMap<>();

    private static JavaPlugin plugin;

    private static void fillOfflinePlayers() {
        for(final OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            final ExtendedUser user = new CraftUser(player.getUniqueId());
            users.putIfAbsent(user.getUUID(), user);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init(final @NotNull JavaPlugin pl) {
        plugin = pl;
        UserAdapter.setAdapter(ADAPTER);
        final File usersFolder = new File(pl.getDataFolder(), "users");
        if(!usersFolder.exists()) usersFolder.mkdirs();

        final File[] userFiles = usersFolder.listFiles();
        if(userFiles == null) {
            fillOfflinePlayers();
            return;
        }

        for(final File userFile : userFiles) {
            final FileConfiguration configuration = YamlConfiguration.loadConfiguration(userFile);
            final ExtendedUser user = (ExtendedUser) configuration.get("data");
            users.put(user.getUUID(), user);
        }
        fillOfflinePlayers();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void save() {
        final File usersFolder = new File(plugin.getDataFolder(), "users");
        if(!usersFolder.exists()) usersFolder.mkdirs();

        for(final Entry<UUID, ExtendedUser> entry : users.entrySet()) {
            try {
                final File userFile = new File(usersFolder, entry.getKey().toString() + ".yml");
                if(!userFile.exists()) {
                    userFile.createNewFile();
                }

                final FileConfiguration configuration = YamlConfiguration.loadConfiguration(userFile);
                configuration.set("data", entry.getValue());
                configuration.save(userFile);
            } catch(final IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void addUser(final UUID uuid) {
        users.putIfAbsent(uuid, new CraftUser(uuid));
    }

    @Override
    public @NotNull ExtendedUser getUser(final @NotNull UUID uuid) {
        return users.get(uuid);
    }

    @Override
    public @NotNull ExtendedUser getUser(final @NotNull OfflinePlayer player) {
        return users.get(player.getUniqueId());
    }

    @Override
    public @NotNull Property<ExtendedUser> getUserOption(final @NotNull UUID uuid) {
        return Property.of(users.get(uuid));
    }

    @Override
    public @NotNull Property<ExtendedUser> getUserOption(final @NotNull OfflinePlayer player) {
        return Property.of(users.get(player.getUniqueId()));
    }

}
