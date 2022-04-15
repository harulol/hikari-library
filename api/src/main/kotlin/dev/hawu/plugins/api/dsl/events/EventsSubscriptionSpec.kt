package dev.hawu.plugins.api.dsl.events

import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.events.Events
import org.bukkit.event.Event
import org.bukkit.plugin.java.JavaPlugin

/**
 * Specification for an event subscription.
 */
@ScopeControlMarker
class EventsSubscriptionSpec<T : Event> internal constructor(eventClass: Class<T>) {

    private val underlyingBuilder = Events.newSubscription(eventClass)

    val plugin = MutableProperty.of<JavaPlugin>(null)

    /**
     * Open the options specification for this event subscription
     * spec.
     */
    fun options(spec: EventsSubscriptionOptionsSpec<T>.() -> Unit) {
        val options = EventsSubscriptionOptionsSpec<T>().apply(spec)
        options.applyTo(underlyingBuilder)
    }

    /**
     * The action to do when the event happens.
     */
    fun action(spec: EventsSubscriptionActionSpec<T>.() -> Unit) {
        underlyingBuilder.openHandler { listener, event ->
            EventsSubscriptionActionSpec(event, listener).apply(spec)
        }
    }

    /**
     * The filter to apply to the event.
     */
    fun filter(spec: EventsSubscriptionActionSpec<T>.() -> Boolean) {
        underlyingBuilder.openFilter { listener, event -> EventsSubscriptionActionSpec(event, listener).run(spec) }
    }

    internal fun build() {
        if(!plugin.isPresent)
            throw IllegalStateException("Plugin must be set before building the subscription.")
        underlyingBuilder.build(plugin.get())
    }

}
