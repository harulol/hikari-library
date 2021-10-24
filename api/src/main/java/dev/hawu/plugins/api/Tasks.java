package dev.hawu.plugins.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Utility class for scheduling tasks with a known plugin constant.
 *
 * @since 1.0
 */
public final class Tasks {

    private Tasks() {}

    /**
     * Schedule a runnable.
     *
     * @param type     The type of the task.
     * @param plugin   The plugin to schedule with.
     * @param consumer The consumer that takes in the runnable.
     * @return The link {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask schedule(@NotNull final TaskType type, @NotNull final JavaPlugin plugin, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return schedule(type, plugin, 0, 0, consumer);
    }

    @NotNull
    private static BukkitRunnable consume(@NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                Objects.requireNonNull(consumer, "Consumer to handle BukkitRunnable can not be null.").accept(this);
            }
        };
    }

    /**
     * Tells the Bukkit Scheduler to cancel all tasks scheduled
     * by the plugin.
     *
     * @param plugin The plugin whose tasks to cancel.
     * @since 1.0
     */
    public static void cancelAllTasks(final @NotNull JavaPlugin plugin) {
        Bukkit.getScheduler().cancelTasks(plugin);
    }

    /**
     * Schedules a runnable that runs instantly synchronously.
     *
     * @param plugin   The plugin to schedule with.
     * @param consumer The consumer that takes in the runnable.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask schedule(@NotNull final JavaPlugin plugin, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTask(plugin);
    }

    /**
     * Schedules a runnable that runs instantly and asynchronously.
     *
     * @param plugin   The plugin to schedule with.
     * @param consumer The consumer that takes in the runnable.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleAsync(@NotNull final JavaPlugin plugin, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskAsynchronously(plugin);
    }

    /**
     * Schedules a runnable that runs in the future after a certain number of ticks.
     *
     * @param plugin   The plugin to schedule with.
     * @param delay    Amount of ticks to wait.
     * @param consumer The consumer that takes in the runnable.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleLater(@NotNull final JavaPlugin plugin, final long delay, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskLater(plugin, delay);
    }

    /**
     * Schedules a runnable that runs in the future after a certain number of ticks,
     * and this runnable executes the code asynchronously.
     *
     * @param plugin   The plugin to schedule with.
     * @param delay    Amount of ticks to wait.
     * @param consumer The consumer that takes in the runnable.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleLaterAsync(@NotNull final JavaPlugin plugin, final long delay, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskLaterAsynchronously(plugin, delay);
    }

    /**
     * Schedules a runnable that runs in the future after a certain number of ticks,
     * repeatedly in intervals.
     *
     * @param plugin   The plugin to schedule with.
     * @param delay    Amount of ticks to wait until the first execution.
     * @param interval Amount of ticks to wait between executions.
     * @param consumer The consumer that takes in the runnable every run.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleTimer(@NotNull final JavaPlugin plugin, final long delay, final long interval, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskTimer(plugin, delay, interval);
    }

    /**
     * Schedules a runnable that runs in the future after a certain number of ticks,
     * repeatedly in intervals and all executions will be done asynchronously.
     *
     * @param plugin   The plugin to schedule with.
     * @param delay    Amount of ticks to wait until the first execution.
     * @param interval Amount of ticks to wait between executions.
     * @param consumer The consumer that takes in the runnable every run.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleTimerAsync(@NotNull final JavaPlugin plugin, final long delay, final long interval,
                                                @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskTimerAsynchronously(plugin, delay, interval);
    }

    /**
     * Schedule a runnable.
     *
     * @param type     The type of the task.
     * @param plugin   The plugin to schedule with.
     * @param consumer The consumer that takes in the runnable.
     * @param delay    Amount of ticks to wait until first execution.
     * @return The link {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask schedule(@NotNull final TaskType type, @NotNull final JavaPlugin plugin, final long delay,
                                      @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return schedule(type, plugin, delay, 0, consumer);
    }

    /**
     * Schedules a runnable.
     *
     * @param type     The type of the task.
     * @param plugin   The plugin to schedule with.
     * @param delay    Amount of ticks to wait until the first execution.
     * @param interval Amount of ticks to wait between executions.
     * @param consumer The consumer that takes in the runnable.
     * @return The link {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask schedule(@NotNull final TaskType type, @NotNull final JavaPlugin plugin, final long delay,
                                      final long interval, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        switch(type) {
            case ASYNC:
                return scheduleAsync(plugin, consumer);
            case TIMER_SYNC:
                return scheduleTimer(plugin, delay, interval, consumer);
            case TIMER_ASYNC:
                return scheduleTimerAsync(plugin, delay, interval, consumer);
            case LATER_SYNC:
                return scheduleLater(plugin, delay, consumer);
            case LATER_ASYNC:
                return scheduleLaterAsync(plugin, delay, consumer);
            default:
                return schedule(plugin, consumer);
        }
    }

    /**
     * The type of the task to schedule.
     *
     * @since 1.1
     */
    public enum TaskType {

        /**
         * A task that runs on the main thread immediately
         * that might block the server.
         *
         * @since 1.1
         */
        SYNC,

        /**
         * A task that runs asynchronously and immediately.
         * <p>
         * <strong>Warning</strong>: Bukkit discourages the usages
         * of their APIs from async tasks.
         *
         * @since 1.1
         */
        ASYNC,

        /**
         * A task that runs on the main thread after a certain
         * amount of time.
         *
         * @since 1.1
         */
        LATER_SYNC,

        /**
         * A task that runs asynchronously after a certain amount
         * of time.
         * <p>
         * <strong>Warning</strong>: Bukkit discourages the usages
         * of their APIs from async tasks.
         *
         * @since 1.1
         */
        LATER_ASYNC,

        /**
         * A task that runs on the main thread after a certain amount
         * of time, and will continue to run in intervals until the registering
         * plugin is disabled or the task is cancelled.
         *
         * @since 1.1
         */
        TIMER_SYNC,

        /**
         * A task that runs asynchronously after a certain amount of
         * time, and will continue to run in intervals until the registering
         * plugin is disabled or the task is cancelled.
         * <p>
         * <strong>Warning</strong>: Bukkit discourages the usages of their
         * APIs from async tasks.
         *
         * @since 1.1
         */
        TIMER_ASYNC

    }

}
