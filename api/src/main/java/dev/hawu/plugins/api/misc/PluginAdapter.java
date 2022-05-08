package dev.hawu.plugins.api.misc;

import dev.hawu.plugins.api.exceptions.AlreadyInitializedException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an adapter that holds the main library plugin instance
 * to default to in some features.
 *
 * @since 1.6
 */
public final class PluginAdapter {

    private static JavaPlugin plugin;

    private PluginAdapter() {
    }

    /**
     * Retrieves the plugin instance.
     *
     * @return The plugin instance.
     * @since 1.6
     */
    @NotNull
    public static JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Sets the plugin if it's not already set.
     *
     * @param pl The plugin to set.
     * @since 1.6
     */
    public static void setPlugin(final @NotNull JavaPlugin pl) {
        if(plugin != null) throw new AlreadyInitializedException();
        plugin = pl;
    }

}
