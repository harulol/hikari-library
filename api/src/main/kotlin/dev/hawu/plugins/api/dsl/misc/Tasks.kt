package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.Tasks
import dev.hawu.plugins.api.Tasks.TaskType
import dev.hawu.plugins.api.dsl.tasks.TaskSpec
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

/**
 * Schedules a task.
 */
fun schedule(plugin: JavaPlugin, async: Boolean = false, delay: Long = -1, interval: Long = -1, runnable: TaskSpec.() -> Unit): BukkitTask =
    when {
        delay >= 0 && interval >= 0 -> scheduleTimer(plugin, async, delay, interval, runnable)
        delay >= 0 -> scheduleLater(plugin, async, delay, runnable)
        else -> scheduleTask(plugin, async, runnable)
    }

private fun scheduleTask(plugin: JavaPlugin, async: Boolean = false, runnable: TaskSpec.() -> Unit): BukkitTask =
    Tasks.schedule(if(async) TaskType.ASYNC else TaskType.SYNC, plugin) {
        try {
            TaskSpec(it).runnable()
        } catch(e: InterruptedException) {
            // ignore
        }
    }

private fun scheduleLater(plugin: JavaPlugin, async: Boolean = false, delay: Long, runnable: TaskSpec.() -> Unit): BukkitTask =
    Tasks.schedule(if(async) TaskType.LATER_ASYNC else TaskType.LATER_SYNC, plugin, delay) {
        try {
            TaskSpec(it).runnable()
        } catch(e: InterruptedException) {
            // Ignore.
        }
    }

private fun scheduleTimer(plugin: JavaPlugin, async: Boolean = false, delay: Long, interval: Long, runnable: TaskSpec.() -> Unit): BukkitTask {
    var count = 1
    return Tasks.schedule(if(async) TaskType.TIMER_ASYNC else TaskType.TIMER_SYNC, plugin, delay, interval) {
        try {
            TaskSpec(it, count++).runnable()
        } catch(e: InterruptedException) {
            // Ignore.
        }
    }
}
