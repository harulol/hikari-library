package dev.hawu.plugins.api.dsl.time

import dev.hawu.plugins.api.TimeConversions
import dev.hawu.plugins.api.TimeUnit
import dev.hawu.plugins.api.TimeUnit.DAY
import dev.hawu.plugins.api.TimeUnit.HOUR
import dev.hawu.plugins.api.TimeUnit.MICROSECOND
import dev.hawu.plugins.api.TimeUnit.MILLISECOND
import dev.hawu.plugins.api.TimeUnit.MINUTE
import dev.hawu.plugins.api.TimeUnit.MONTH
import dev.hawu.plugins.api.TimeUnit.NANOSECOND
import dev.hawu.plugins.api.TimeUnit.SECOND
import dev.hawu.plugins.api.TimeUnit.WEEK
import dev.hawu.plugins.api.dsl.ScopeControlMarker

/**
 * Specification to build a time in milliseconds
 * by adding up.
 */
@ScopeControlMarker
class TimeMillisSpec internal constructor() {

    private val underlyingBuilder = TimeConversions.buildMillis()

    val nanosecond: TimeUnitValue = TimeValue(NANOSECOND, false)
    val nanoseconds: TimeUnitValue = TimeValue(NANOSECOND, true)
    val microsecond: TimeUnitValue = TimeValue(MICROSECOND, false)
    val microseconds: TimeUnitValue = TimeValue(MICROSECOND, true)
    val millisecond: TimeUnitValue = TimeValue(MILLISECOND, false)
    val milliseconds: TimeUnitValue = TimeValue(MILLISECOND, true)
    val second: TimeUnitValue = TimeValue(SECOND, false)
    val seconds: TimeUnitValue = TimeValue(SECOND, true)
    val minute: TimeUnitValue = TimeValue(MINUTE, false)
    val minutes: TimeUnitValue = TimeValue(MINUTE, true)
    val hour: TimeUnitValue = TimeValue(HOUR, false)
    val hours: TimeUnitValue = TimeValue(HOUR, true)
    val day: TimeUnitValue = TimeValue(DAY, false)
    val days: TimeUnitValue = TimeValue(DAY, true)
    val week: TimeUnitValue = TimeValue(WEEK, false)
    val weeks: TimeUnitValue = TimeValue(WEEK, true)
    val month: TimeUnitValue = TimeValue(MONTH, false)
    val months: TimeUnitValue = TimeValue(MONTH, true)

    /**
     * Infix function to add a time unit to the specification.
     *
     * Alias for [add].
     *
     * @throws IllegalArgumentException if the time unit is used wrong
     */
    infix fun Number.more(unit: TimeUnitValue) {
        val timeValue = unit as TimeValue

        if(timeValue.plural && this == 1)
            throw IllegalArgumentException("For value of 1, use the singular time unit :)")
        else if(!timeValue.plural && this != 1)
            throw IllegalArgumentException("For values that are not 1, use the plural time unit :)")

        add(timeValue.unit, this)
    }

    /**
     * Adds some time to the underlying builder.
     */
    fun add(unit: TimeUnit, value: Number) {
        when(unit) {
            NANOSECOND -> underlyingBuilder.nanoseconds(value)
            MICROSECOND -> underlyingBuilder.microseconds(value)
            MILLISECOND -> underlyingBuilder.milliseconds(value)
            SECOND -> underlyingBuilder.seconds(value)
            MINUTE -> underlyingBuilder.minutes(value)
            HOUR -> underlyingBuilder.hours(value)
            DAY -> underlyingBuilder.days(value)
            WEEK -> underlyingBuilder.weeks(value)
            MONTH -> underlyingBuilder.months(value)
        }
    }

    /**
     * Adds some time formatted in a string.
     */
    operator fun String.unaryPlus() {
        underlyingBuilder.fromString(this)
    }

    internal fun build(): Double {
        return underlyingBuilder.build()
    }

    sealed interface TimeUnitValue
    private class TimeValue(val unit: TimeUnit, val plural: Boolean) : TimeUnitValue

}
