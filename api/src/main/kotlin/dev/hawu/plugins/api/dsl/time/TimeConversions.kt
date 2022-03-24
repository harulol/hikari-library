package dev.hawu.plugins.api.dsl.time

/**
 * Constructs a spec to build time in milliseconds.
 */
fun timeMillis(spec: TimeMillisSpec.() -> Unit) = TimeMillisSpec().apply(spec).build()

/**
 * Constructs a spec to build a formatted time string.
 */
fun timeString(spec: TimeStringSpec.() -> Unit) = TimeStringSpec().apply(spec).build()
