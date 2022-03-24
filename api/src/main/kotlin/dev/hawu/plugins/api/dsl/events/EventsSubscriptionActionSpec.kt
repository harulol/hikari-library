package dev.hawu.plugins.api.dsl.events

import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.events.listener.ClosableListener
import org.bukkit.event.Event

/**
 * The spec for handling an event caught in a subscription.
 */
@ScopeControlMarker
class EventsSubscriptionActionSpec<T : Event> internal constructor(
    val event: T,
    val listener: ClosableListener,
)
