package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.dsl.tasks.TaskSpec
import dev.hawu.plugins.api.adapters.PluginAdapter
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * Schedules a task.
 */
fun schedule(async: Boolean = false, delay: Long = -1, interval: Long = -1, runnable: TaskSpec.() -> Unit): BukkitTask =
    when {
        interval >= 0 -> scheduleTimer(async, delay.coerceAtLeast(0), interval, runnable)
        delay >= 0 -> scheduleLater(async, delay, runnable)
        else -> scheduleTask(async, runnable)
    }

private fun scheduleTask(async: Boolean = false, runnable: TaskSpec.() -> Unit): BukkitTask {
    val run = object : BukkitRunnable() {
        override fun run() {
            TaskSpec(this).runnable()
        }
    }
    return if(async) run.runTaskAsynchronously(PluginAdapter.getPlugin())
    else run.runTask(PluginAdapter.getPlugin())
}

private fun scheduleLater(async: Boolean = false, delay: Long, runnable: TaskSpec.() -> Unit): BukkitTask {
    val run = object : BukkitRunnable() {
        override fun run() {
            TaskSpec(this).runnable()
        }
    }
    return if(async) run.runTaskLaterAsynchronously(PluginAdapter.getPlugin(), delay)
    else run.runTaskLater(PluginAdapter.getPlugin(), delay)
}

private fun scheduleTimer(async: Boolean = false, delay: Long, interval: Long, runnable: TaskSpec.() -> Unit): BukkitTask {
    var count = 1
    val run = object : BukkitRunnable() {
        override fun run() {
            TaskSpec(this, count++).runnable()
        }
    }
    return if(async) run.runTaskTimerAsynchronously(PluginAdapter.getPlugin(), delay, interval)
    else run.runTaskTimer(PluginAdapter.getPlugin(), delay, interval)
}
