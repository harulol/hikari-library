package dev.hawu.plugins.api.dsl.events

import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.events.listener.ClosableListener
import org.bukkit.event.Cancellable
import org.bukkit.event.Event

/**
 * The spec for handling an event caught in a subscription.
 */
@ScopeControlMarker
class EventsSubscriptionActionSpec<T : Event> internal constructor(
    val event: T,
    val listener: ClosableListener,
) {

    /**
     * Cancels the event if it is an instance of [Cancellable].
     */
    val cancel: Unit
        get() {
            if(event is Cancellable) event.isCancelled = true
        }

}
