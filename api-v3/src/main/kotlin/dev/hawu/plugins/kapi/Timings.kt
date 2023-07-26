package dev.hawu.plugins.kapi

import kotlin.math.floor
import kotlin.math.roundToLong

/**
 * A class that represents a time unit.
 */
enum class TimeUnit(val value: Double, val suffix: String) {

    NANOSECOND(1e-6, "ns"),
    MICROSECOND(1e-3, "µs"),
    MILLISECOND(1.0, "ms"),
    SECOND(1e3, "s"),
    MINUTE(6e4, "m"),
    HOUR(3.6e6, "h"),
    DAY(8.64e7, "d"),
    WEEK(6.048e8, "w"),
    MONTH(2.628e9, "mo"),
    YEAR(3.154e10, "y");

    val singular = this.name.lowercase()
    val plural = "${this.name.lowercase()}s"

    /**
     * Returns true if this time unit is ignored.
     */
    val isIgnored: Boolean
        get() = this in unused

    /**
     * Returns the time unit before.
     */
    operator fun minus(num: Int): TimeUnit = values().getOrElse(this.ordinal - num) { this }

    companion object {

        /**
         * A list of all the unused time units.
         */
        private val unused = arrayOf(WEEK)

        /**
         * A list of all the time units in reverse order.
         */
        val reversed = values().reversedArray()

        /**
         * A map of all the time units and their names.
         */
        private val namesMatcher =
            listOf("ns", "nano", "nanos", "nanosecond", "nanoseconds").associateWith { NANOSECOND } +
                    listOf(
                        "µs",
                        "mms",
                        "micro",
                        "micros",
                        "microsecond",
                        "microseconds"
                    ).associateWith { MICROSECOND } +
                    listOf("ms", "milli", "millis", "millisecond", "milliseconds").associateWith { MILLISECOND } +
                    listOf("s", "sec", "secs", "second", "seconds").associateWith { SECOND } +
                    listOf("m", "min", "mins", "minute", "minutes").associateWith { MINUTE } +
                    listOf("h", "hr", "hrs", "hour", "hours").associateWith { HOUR } +
                    listOf("d", "day", "days").associateWith { DAY } +
                    listOf("w", "wk", "wks", "week", "weeks").associateWith { WEEK } +
                    listOf("mo", "month", "months").associateWith { MONTH } +
                    listOf("y", "yr", "yrs", "year", "years").associateWith { YEAR }

        /**
         * Parses a string into a time unit.
         */
        fun parse(string: String): TimeUnit? = namesMatcher[string]

    }

}

val matcher = Regex("(\\d*\\.?\\d+)(\\w+)")

/**
 * Converts a string to milliseconds.
 */
tailrec fun String.toMillis(result: MatchResult? = matcher.matchAt(this, 0), acc: Double = 0.0): Long {
    if (result == null || result.groupValues.size != 3)
        return acc.roundToLong()
    val (number, unit) = result.destructured
    val timeUnit = TimeUnit.parse(unit) ?: return toMillis(result.next(), acc)
    val value = number.toDoubleOrNull() ?: return toMillis(result.next(), acc)
    return toMillis(result.next(), acc + value * timeUnit.value)
}

/**
 * Converts a long to a time string.
 */
tailrec fun Long.toTimeString(
    until: TimeUnit = TimeUnit.SECOND, unit: TimeUnit = TimeUnit.YEAR,
    spaces: Boolean = false, full: Boolean = false, cur: String = "",
): String {
    if (this == 0L || unit < until)
        return cur.trim().ifEmpty { "0s" }

    val value = floor(this / unit.value).toLong()
    val space = if (spaces && cur.isNotEmpty()) " " else ""
    val name = when {
        full && value == 1L -> " ${unit.singular}"
        full -> " ${unit.plural}"
        else -> unit.suffix
    }
    val new = "$cur$space${value.format()}$name"

    if (value == 0L || unit.isIgnored)
        return toTimeString(until, unit - 1, spaces, full, cur)
    return toTimeString(until, unit - 1, spaces, full, new)
}
