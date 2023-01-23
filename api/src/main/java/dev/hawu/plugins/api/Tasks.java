package dev.hawu.plugins.api;

import dev.hawu.plugins.api.misc.TaskBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Utility class for quickly scheduling a task using
 * BukkitRunnables.
 *
 * @since 2.0
 */
public final class Tasks {

    private Tasks() {}

    /**
     * Calls up a task builder using the provided bukkit runnable.
     *
     * @param consumer the consumer that accepts the runnable
     * @return the task builder
     * @since 2.0
     */
    @NotNull
    public static TaskBuilder run(final @NotNull Consumer<BukkitRunnable> consumer) {
        return new TaskBuilder(consumer);
    }

    /**
     * Calls up a task builder using the provided runnable.
     *
     * @param runnable the runnable
     * @return the task builder
     * @since 2.0
     */
    @NotNull
    public static TaskBuilder run(final @NotNull Runnable runnable) {
        return run(bukkitRunnable -> runnable.run());
    }

    /**
     * Cancels all tasks related to the provided plugin.
     *
     * @param plugin the plugin to cancel for
     * @since 2.0
     */
    public static void cancelAllTasks(final @NotNull JavaPlugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

}
