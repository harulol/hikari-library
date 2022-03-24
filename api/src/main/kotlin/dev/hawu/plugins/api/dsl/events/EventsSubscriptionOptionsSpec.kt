package dev.hawu.plugins.api.dsl.events

import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.events.EventSubscriptionBuilder
import org.bukkit.event.EventPriority

/**
 * Specification for creating a subscription to events
 * dynamically.
 */
@ScopeControlMarker
class EventsSubscriptionOptionsSpec internal constructor() {

    private var _ignoreCancelled = false
    private var _countsOnFiltered = false

    /**
     * The priority to register the listener at.
     * The lower, the sooner it is called.
     */
    val priority = MutableProperty.of(EventPriority.NORMAL)

    /**
     * Do not listen for cancelled events.
     */
    val ignoreCancelled: Unit
        get() {
            _ignoreCancelled = true
        }

    /**
     * Ignore all events that don't satisfy this predicate.
     */
    val predicate = MutableProperty.of<(Any) -> Boolean> { true }

    /**
     * Still counts the invocation as valid even if it does not
     * satisfy [predicate].
     */
    val countsOnFiltered: Unit
        get() {
            _countsOnFiltered = true
        }

    /**
     * The number of invocations this listener will listen for,
     * after which, it will close itself.
     */
    val invocations = MutableProperty.of(-1L)

    /**
     * The amount of time this listener will listen for,
     * after which, it will close itself.
     */
    val time = MutableProperty.of(-1L)

    internal fun applyTo(builder: EventSubscriptionBuilder<*>) {
        builder.priority(priority.get())
        if(_ignoreCancelled) builder.ignoreCancelled()
        builder.filter { predicate.get()(it) }
        if(_countsOnFiltered) builder.countsIfFiltered()
        builder.expiresAfterInvocations(invocations.get())
        builder.expiresAfter(time.get())
    }

}
