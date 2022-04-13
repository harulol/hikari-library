package dev.hawu.plugins.api.dsl.tasks

import dev.hawu.plugins.api.dsl.ScopeControlMarker
import org.bukkit.scheduler.BukkitRunnable

/**
 * It's literally nothing but something to hold the variables
 * task and [stop] for cleaner task scheduling.
 *
 * @property count the number of times this task has run. doesn't matter if it's not a timer task.
 */
@ScopeControlMarker
class TaskSpec internal constructor(val task: BukkitRunnable, val count: Int = 1) {

    /**
     * Stop the execution.
     */
    val stop: Nothing
        get() = throw InterruptedException("Stop the execution.")

    /**
     * Stop the execution and cancel the task.
     */
    val cancel: Nothing
        get() {
            task.cancel()
            throw InterruptedException("Cancel the execution.")
        }

}
