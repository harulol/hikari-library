package dev.hawu.plugins.api.misc;

import dev.hawu.plugins.api.Strings;
import org.bukkit.Bukkit;
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
     * Logs a default green message, can be changed.
     *
     * @param message The message to log.
     * @since 1.7
     */
    public static void fine(final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&a" + message));
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
     * Logs a default red message, can be changed.
     *
     * @param message The message to log.
     * @since 1.7
     */
    public static void severe(final @NotNull String message) {
        Bukkit.getConsoleSender().sendMessage(Strings.color("&c" + message));
    }

}
