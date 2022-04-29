package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.dsl.tasks.TaskSpec
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * Schedules a task.
 */
fun schedule(plugin: JavaPlugin, async: Boolean = false, delay: Long = -1, interval: Long = -1, runnable: TaskSpec.() -> Unit): BukkitTask =
    when {
        interval >= 0 -> scheduleTimer(plugin, async, delay.coerceAtLeast(0), interval, runnable)
        delay >= 0 -> scheduleLater(plugin, async, delay, runnable)
        else -> scheduleTask(plugin, async, runnable)
    }

private fun scheduleTask(plugin: JavaPlugin, async: Boolean = false, runnable: TaskSpec.() -> Unit): BukkitTask {
    val run = object : BukkitRunnable() {
        override fun run() {
            try {
                TaskSpec(this).runnable()
            } catch(e: InterruptedException) {
                // Ignore.
            }
        }
    }
    return if(async) run.runTaskAsynchronously(plugin)
    else run.runTask(plugin)
}

private fun scheduleLater(plugin: JavaPlugin, async: Boolean = false, delay: Long, runnable: TaskSpec.() -> Unit): BukkitTask {
    val run = object : BukkitRunnable() {
        override fun run() {
            try {
                TaskSpec(this).runnable()
            } catch(e: InterruptedException) {
                // ignore.
            }
        }
    }
    return if(async) run.runTaskLaterAsynchronously(plugin, delay)
    else run.runTaskLater(plugin, delay)
}

private fun scheduleTimer(plugin: JavaPlugin, async: Boolean = false, delay: Long, interval: Long, runnable: TaskSpec.() -> Unit): BukkitTask {
    var count = 1
    val run = object : BukkitRunnable() {
        override fun run() {
            try {
                TaskSpec(this, count++).runnable()
            } catch(e: InterruptedException) {
                // ignore.
            }
        }
    }
    return if(async) run.runTaskTimerAsynchronously(plugin, delay, interval)
    else run.runTaskTimer(plugin, delay, interval)
}
