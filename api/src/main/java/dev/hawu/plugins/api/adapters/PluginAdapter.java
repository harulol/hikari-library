package dev.hawu.plugins.api.adapters;

import dev.hawu.plugins.api.exceptions.AlreadyInitializedException;
import dev.hawu.plugins.api.i18n.LanguageModule;
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
    private static LanguageModule module;

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
     * Retrieves the main language module of the library.
     *
     * @return the language module
     * @since 2.0
     */
    @NotNull
    public static LanguageModule getModule() {
        return module;
    }

    /**
     * Sets the plugin if it's not already set.
     *
     * @param pl The plugin to set.
     * @since 1.6
     */
    public static void setPlugin(final @NotNull JavaPlugin pl, final @NotNull LanguageModule mod) {
        if(plugin != null || module != null) throw new AlreadyInitializedException();
        plugin = pl;
        module = mod;
    }

}
