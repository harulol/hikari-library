package dev.hawu.plugins.api;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The simple class that holds constants that have to be used
 * across submodules, so the constants only have to be registered
 * and initialized once.
 *
 * @since 1.0
 */
public final class Constants {

    private static JavaPlugin plugin;

    private Constants() {}

    /**
     * Overrides the plugin instance if and only if {@link Constants#plugin} is {@code null}.
     *
     * @param pl The plugin to initialize with.
     * @since 1.0
     */
    public static void setPlugin(@NotNull final JavaPlugin pl) {
        if(plugin != null) plugin = Objects.requireNonNull(pl, "Plugin to initialize with must be non-null.");
    }

    /**
     * Retrieves the plugin instance if it is initialized.
     *
     * @return The initialized plugin instance.
     * @since 1.0
     */
    @NotNull
    public static JavaPlugin getPlugin() {
        if(plugin == null) throw new UnsupportedOperationException("Plugin is not yet initialized.");
        return plugin;
    }

}
