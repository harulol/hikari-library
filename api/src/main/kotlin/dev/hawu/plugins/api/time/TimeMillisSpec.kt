package dev.hawu.plugins.api.time

import dev.hawu.plugins.api.TimeConversions
import dev.hawu.plugins.api.TimeUnit
import dev.hawu.plugins.api.dsl.ScopeControlMarker

/**
 * Specification to build a time in milliseconds
 * by adding up.
 */
@ScopeControlMarker
class TimeMillisSpec internal constructor() {

    private val underlyingBuilder = TimeConversions.buildMillis()

    val nanosecond: TimeUnitValue = TimeValue(TimeUnit.NANOSECOND, false)
    val nanoseconds: TimeUnitValue = TimeValue(TimeUnit.NANOSECOND, true)
    val microsecond: TimeUnitValue = TimeValue(TimeUnit.MICROSECOND, false)
    val microseconds: TimeUnitValue = TimeValue(TimeUnit.MICROSECOND, true)
    val millisecond: TimeUnitValue = TimeValue(TimeUnit.MILLISECOND, false)
    val milliseconds: TimeUnitValue = TimeValue(TimeUnit.MILLISECOND, true)
    val second: TimeUnitValue = TimeValue(TimeUnit.SECOND, false)
    val seconds: TimeUnitValue = TimeValue(TimeUnit.SECOND, true)
    val minute: TimeUnitValue = TimeValue(TimeUnit.MINUTE, false)
    val minutes: TimeUnitValue = TimeValue(TimeUnit.MINUTE, true)
    val hour: TimeUnitValue = TimeValue(TimeUnit.HOUR, false)
    val hours: TimeUnitValue = TimeValue(TimeUnit.HOUR, true)
    val day: TimeUnitValue = TimeValue(TimeUnit.DAY, false)
    val days: TimeUnitValue = TimeValue(TimeUnit.DAY, true)
    val week: TimeUnitValue = TimeValue(TimeUnit.WEEK, false)
    val weeks: TimeUnitValue = TimeValue(TimeUnit.WEEK, true)
    val month: TimeUnitValue = TimeValue(TimeUnit.MONTH, false)
    val months: TimeUnitValue = TimeValue(TimeUnit.MONTH, true)

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
            TimeUnit.NANOSECOND -> underlyingBuilder.nanoseconds(value)
            TimeUnit.MICROSECOND -> underlyingBuilder.microseconds(value)
            TimeUnit.MILLISECOND -> underlyingBuilder.milliseconds(value)
            TimeUnit.SECOND -> underlyingBuilder.seconds(value)
            TimeUnit.MINUTE -> underlyingBuilder.minutes(value)
            TimeUnit.HOUR -> underlyingBuilder.hours(value)
            TimeUnit.DAY -> underlyingBuilder.days(value)
            TimeUnit.WEEK -> underlyingBuilder.weeks(value)
            TimeUnit.MONTH -> underlyingBuilder.months(value)
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
