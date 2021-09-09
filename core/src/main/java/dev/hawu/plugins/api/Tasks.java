package dev.hawu.plugins.api;

import org.bukkit.Bukkit;
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
     * by the plugin in {@link Constants}.
     *
     * @since 1.0
     */
    public static void cancelAllTasks() {
        Bukkit.getScheduler().cancelTasks(Constants.getPlugin());
    }

    /**
     * Schedules a runnable that runs instantly synchronously.
     *
     * @param consumer The consumer that takes in the runnable.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask schedule(@NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTask(Constants.getPlugin());
    }

    /**
     * Schedules a runnable that runs instantly and asynchronously.
     *
     * @param consumer The consumer that takes in the runnable.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleAsync(@NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskAsynchronously(Constants.getPlugin());
    }

    /**
     * Schedules a runnable that runs in the future after a certain number of ticks.
     *
     * @param delay    Amount of ticks to wait.
     * @param consumer The consumer that takes in the runnable.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleLater(final long delay, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskLater(Constants.getPlugin(), delay);
    }

    /**
     * Schedules a runnable that runs in the future after a certain number of ticks,
     * and this runnable executes the code asynchronously.
     *
     * @param delay    Amount of ticks to wait.
     * @param consumer The consumer that takes in the runnable.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleLaterAsync(final long delay, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskLaterAsynchronously(Constants.getPlugin(), delay);
    }

    /**
     * Schedules a runnable that runs in the future after a certain number of ticks,
     * repeatedly in intervals.
     *
     * @param delay    Amount of ticks to wait until the first execution.
     * @param interval Amount of ticks to wait between executions.
     * @param consumer The consumer that takes in the runnable every run.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleTimer(final long delay, final long interval, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskTimer(Constants.getPlugin(), delay, interval);
    }

    /**
     * Schedules a runnable that runs in the future after a certain number of ticks,
     * repeatedly in intervals and all executions will be done asynchronously.
     *
     * @param delay    Amount of ticks to wait until the first execution.
     * @param interval Amount of ticks to wait between executions.
     * @param consumer The consumer that takes in the runnable every run.
     * @return The {@link BukkitTask} after scheduling.
     * @since 1.0
     */
    @NotNull
    public static BukkitTask scheduleTimerAsync(final long delay, final long interval, @NotNull final Consumer<@NotNull BukkitRunnable> consumer) {
        return consume(consumer).runTaskTimerAsynchronously(Constants.getPlugin(), delay, interval);
    }

}
