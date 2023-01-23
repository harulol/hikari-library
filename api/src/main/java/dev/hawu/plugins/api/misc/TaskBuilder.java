package dev.hawu.plugins.api.misc;

import dev.hawu.plugins.api.adapters.PluginAdapter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The builder structure for building a task executor.
 *
 * @since 2.0
 */
public final class TaskBuilder {

    private final Consumer<BukkitRunnable> consumer;
    private JavaPlugin plugin = PluginAdapter.getPlugin();
    private long period = -1;
    private long delay = -1;
    private boolean async = false;

    /**
     * Creates a new builder for a task using the provided consumer.
     *
     * @param consumer the consumer
     * @since 2.0
     */
    public TaskBuilder(final @NotNull Consumer<BukkitRunnable> consumer) {
        this.consumer = consumer;
    }

    /**
     * Configures the plugin to be bound to for the task.
     * This uses the library's instance by default.
     *
     * @param plugin the plugin to be bound to
     * @return the same builder
     * @since 2.0
     */
    @NotNull
    public TaskBuilder plugin(final JavaPlugin plugin) {
        this.plugin = plugin;
        return this;
    }

    /**
     * Configures the interval time in ticks for this task.
     *
     * @param period the period in ticks
     * @return the same builder
     * @since 2.0
     */
    @NotNull
    public TaskBuilder period(final long period) {
        this.period = period;
        return this;
    }

    /**
     * Configures the delay in ticks for this task.
     *
     * @param delay the delay in ticks
     * @return the same builder
     * @since 2.0
     */
    @NotNull
    public TaskBuilder delay(final long delay) {
        this.delay = delay;
        return this;
    }

    /**
     * Configures whether this task should be async or not.
     *
     * @param async whether this should run async
     * @return the same builder
     * @since 2.0
     */
    @NotNull
    public TaskBuilder async(final boolean async) {
        this.async = async;
        return this;
    }

    /**
     * Completes the builder and schedules the BukkitRunnable.
     *
     * @return the bukkit task after scheduling
     * @since 2.0
     */
    @NotNull
    public BukkitTask run() {
        final BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                consumer.accept(this);
            }
        };

        if(delay >= 0 && period >= 0) return async ? runnable.runTaskTimerAsynchronously(plugin, delay, period) : runnable.runTaskTimer(plugin, delay, period);
        else if(delay >= 0) return async ? runnable.runTaskLaterAsynchronously(plugin, delay) : runnable.runTaskLater(plugin, delay);
        else return async ? runnable.runTaskAsynchronously(plugin) : runnable.runTask(plugin);
    }

}
