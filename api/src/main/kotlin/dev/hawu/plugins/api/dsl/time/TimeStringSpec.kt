package dev.hawu.plugins.api.dsl.time

import dev.hawu.plugins.api.TimeConversions
import dev.hawu.plugins.api.TimeUnit
import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.dsl.ScopeControlMarker

/**
 * Specification to build a formatted time string
 * from milliseconds in [Double].
 */
@ScopeControlMarker
class TimeStringSpec internal constructor() {

    private var withSpaces = false
    private var withAbbreviations = false

    /**
     * The time to be translated a formatted string.
     */
    val time = MutableProperty.of<Double>(0.0)

    /**
     * The time unit to stop at.
     */
    val unit = MutableProperty.of(TimeUnit.MILLISECOND)

    /**
     * Add spaces between components.
     * Like `1m20s` to `1m 20s`.
     */
    val spaced: ITimeStringOption
        get() {
            withSpaces = true
            return WithSpacesOption
        }

    /**
     * Whether to display the units in abbreviations.
     */
    val abbreviated: ITimeStringOption
        get() {
            withAbbreviations = true
            return WithAbbreviationsOption
        }

    internal fun build(): String {
        val builder = TimeConversions.buildTimestamp(time.get()).until(unit.get())
        if(withSpaces) builder.withSpaces()
        if(!withAbbreviations) builder.withNoAbbreviations()
        return builder.build()
    }

    /**
     * Public interface to chain options for time string formatter.
     */
    sealed interface ITimeStringOption {
        infix fun and(other: ITimeStringOption) {
            if(other === this) throw IllegalArgumentException("Cannot chain the same option.")
        }
    }

    private object WithSpacesOption : ITimeStringOption
    private object WithAbbreviationsOption : ITimeStringOption

}
