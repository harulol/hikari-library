package dev.hawu.plugins.api;

import dev.hawu.plugins.api.collections.tuples.Pair;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStreamReader;

/**
 * Represents a utility class that helps to translate keys into
 * usable messages.
 *
 * @since 1.0
 */
public final class I18n {

    private final @NotNull JavaPlugin plugin;
    private final @NotNull File messagesFile;
    private final @NotNull FileConfiguration messages;
    private final @NotNull FileConfiguration defaultMessages;

    /**
     * Retrieves an instance of I18n using the plugin.
     *
     * @param plugin The plugin to construct with.
     * @since 1.0
     */
    public I18n(final @NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveResource("messages.yml", false);
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        this.messages = YamlConfiguration.loadConfiguration(this.messagesFile);
        this.defaultMessages = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("messages.yml")));
    }

    private String getValue(final @NotNull FileConfiguration config, final @NotNull String key) {
        if(config.isList(key)) return String.join("\n", config.getStringList(key));
        else return config.getString(key);
    }

    /**
     * Loads the messages again from the file.
     *
     * @since 1.0
     */
    public void reload() {
        // Make sure that file exists.
        if(!messagesFile.exists()) plugin.saveResource("messages.yml", true);
        try {
            messages.load(messagesFile);
        } catch(final Exception ignored) {}
    }

    /**
     * Translates a key to a message and fills in with the placeholder pairs.
     *
     * @param key   The key to translate.
     * @param pairs The placeholders to fill in pairs.
     * @return The translated message, colorized.
     * @since 1.0
     */
    @NotNull
    public String tl(@NotNull final String key, @NotNull final Pair<?, ?> @NotNull ... pairs) {
        final String value;
        if(messages.get(key) == null) {
            if(defaultMessages.get(key) == null) return key;
            else value = getValue(defaultMessages, key);
        } else value = getValue(messages, key);
        return Strings.color(Strings.fillPlaceholders(value, pairs));
    }

}
