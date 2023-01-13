package dev.hawu.plugins.api.misc;

import dev.hawu.plugins.api.Strings;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * A simple utility class for logging in console
 * with colors.
 */
public final class ConsoleLogger {

    private ConsoleLogger() {
    }

    /**
     * Logs a default white message, can be changed.
     *
     * @param message The message to log.
     * @since 1.7
     */
    public static void log(final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&f" + message));
    }

    /**
     * Logs a default white message with a plugin name prefix.
     *
     * @param plugin  The plugin to log with.
     * @param message The message to log.
     * @since 2.0
     */
    public static void log(final @NotNull Plugin plugin, final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&f[" + plugin.getName() + "] " + message));
    }

    /**
     * Logs a default white message formatted.
     *
     * @param message      The message to log.
     * @param replacements The replacements to replace in the message.
     * @since 2.0
     */
    public static void logf(final @NotNull String message, final @NotNull Object... replacements) {
        Bukkit.getConsoleSender().sendMessage(Strings.colorf("&f" + message, replacements));
    }

    /**
     * Logs a default white message formatted with a plugin name prefix.
     *
     * @param plugin       The plugin to log with.
     * @param message      The message to log.
     * @param replacements The replacements to replace in the message.
     * @since 2.0
     */
    public static void logf(final @NotNull Plugin plugin, final @NotNull String message, final @NotNull Object... replacements) {
        Bukkit.getConsoleSender().sendMessage(Strings.colorf("&f[" + plugin.getName() + "] " + message, replacements));
    }

    /**
     * Logs a default green message, can be changed.
     *
     * @param message The message to log.
     * @since 1.7
     */
    public static void fine(final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&a" + message));
    }

    /**
     * Logs a default green message with a plugin name prefix.
     *
     * @param plugin  The plugin to log with.
     * @param message The message to log.
     * @since 2.0
     */
    public static void fine(final @NotNull Plugin plugin, final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&a[" + plugin.getName() + "] " + message));
    }

    /**
     * Logs a default green message formatted.
     *
     * @param message      The message to log.
     * @param replacements The replacements to replace in the message.
     * @since 2.0
     */
    public static void finef(final @NotNull String message, final @NotNull Object... replacements) {
        Bukkit.getConsoleSender().sendMessage(Strings.colorf("&a" + message, replacements));
    }

    /**
     * Logs a default green message formatted with a plugin name prefix.
     *
     * @param plugin       The plugin to log with.
     * @param message      The message to log.
     * @param replacements The replacements to replace in the message.
     * @since 2.0
     */
    public static void finef(final @NotNull Plugin plugin, final @NotNull String message, final @NotNull Object... replacements) {
        Bukkit.getConsoleSender().sendMessage(Strings.colorf("&a[" + plugin.getName() + "] " + message, replacements));
    }

    /**
     * Logs a default yellow message, can be changed.
     *
     * @param message The message to log.
     * @since 1.7
     */
    public static void warn(final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&e" + message));
    }

    /**
     * Logs a default yellow message with a plugin name prefix.
     *
     * @param plugin  The plugin to log with.
     * @param message The message to log.
     * @since 2.0
     */
    public static void warn(final @NotNull Plugin plugin, final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&e[" + plugin.getName() + "] " + message));
    }

    /**
     * Logs a default yellow message formatted.
     *
     * @param message      The message to log.
     * @param replacements The replacements to replace in the message.
     * @since 2.0
     */
    public static void warnf(final @NotNull String message, final @NotNull Object... replacements) {
        Bukkit.getConsoleSender().sendMessage(Strings.colorf("&e" + message, replacements));
    }

    /**
     * Logs a default yellow message formatted with a plugin name prefix.
     *
     * @param plugin       The plugin to log with.
     * @param message      The message to log.
     * @param replacements The replacements to replace in the message.
     * @since 2.0
     */
    public static void warnf(final @NotNull Plugin plugin, final @NotNull String message, final @NotNull Object... replacements) {
        Bukkit.getConsoleSender().sendMessage(Strings.colorf("&e[" + plugin.getName() + "] " + message, replacements));
    }

    /**
     * Logs a default red message, can be changed.
     *
     * @param message The message to log.
     * @since 1.7
     */
    public static void severe(final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&c" + message));
    }

    /**
     * Logs a default red message with a plugin name prefix.
     *
     * @param plugin  The plugin to log with.
     * @param message The message to log.
     * @since 2.0
     */
    public static void severe(final @NotNull Plugin plugin, final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&c[" + plugin.getName() + "] " + message));
    }

    /**
     * Logs a default red message formatted.
     *
     * @param message      The message to log.
     * @param replacements The replacements to replace in the message.
     * @since 2.0
     */
    public static void severef(final @NotNull String message, final @NotNull Object... replacements) {
        Bukkit.getConsoleSender().sendMessage(Strings.colorf("&c" + message, replacements));
    }

    /**
     * Logs a default red message formatted with a plugin name prefix.
     *
     * @param plugin       The plugin to log with.
     * @param message      The message to log.
     * @param replacements The replacements to replace in the message.
     * @since 2.0
     */
    public static void severef(final @NotNull Plugin plugin, final @NotNull String message, final @NotNull Object... replacements) {
        Bukkit.getConsoleSender().sendMessage(Strings.colorf("&c[" + plugin.getName() + "] " + message, replacements));
    }

}
