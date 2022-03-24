package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.dsl.events.EventsSubscriptionSpec
import org.bukkit.event.Event

/**
 * Creates a new subscription of the event in the generic
 * parameter.
 */
inline fun <reified T : Event> subscribe(noinline spec: EventsSubscriptionSpec<T>.() -> Unit) {
    subscribe(T::class.java, spec)
}

/**
 * Creates a new subscription of the event class.
 */
fun <T : Event> subscribe(cls: Class<T>, spec: EventsSubscriptionSpec<T>.() -> Unit) {
    EventsSubscriptionSpec(cls).apply(spec).build()
}
