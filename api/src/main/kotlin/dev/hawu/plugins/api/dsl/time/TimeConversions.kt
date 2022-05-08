package dev.hawu.plugins.api.dsl.time

import dev.hawu.plugins.api.TimeConversions
import dev.hawu.plugins.api.TimeUnit

/**
 * Constructs a spec to build time in milliseconds.
 */
fun timeMillis(spec: TimeMillisSpec.() -> Unit) = TimeMillisSpec().apply(spec).build()

/**
 * Constructs a spec to build a formatted time string.
 */
fun timeString(spec: TimeStringSpec.() -> Unit) = TimeStringSpec().apply(spec).build()

/**
 * Builds a milliseconds time from a string, quickly.
 *
 * Shorthand for:
 * ```
 * TimeConversions.convertToMillis(s)
 * ```
 */
fun String.toMillis() = TimeConversions.convertToMillis(this)

/**
 * Builds a formatted time string from a number, quickly.
 *
 * Shorthand for:
 * ```
 * TimeConversions.convertToReadableFormat(n, unit, true, true)
 * ```
 */
fun Number.toTimeString(until: TimeUnit = TimeUnit.SECOND, spaces: Boolean = true, abbreviated: Boolean = true) =
    TimeConversions.convertToReadableFormat(this.toDouble(), until, spaces, !abbreviated)
