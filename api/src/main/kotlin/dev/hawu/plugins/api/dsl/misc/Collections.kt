package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.collections.Property
import dev.hawu.plugins.api.collections.tuples.Pair as APIPair

/**
 * Sets the value of the property through
 * an invocation.
 */
operator fun <T> Property<T>.invoke(value: T?) {
    set(value)
}

/**
 * Converts a Kotlin pair to a library's pair tuple
 * as some library functions require this version
 * of pair.
 */
fun <A, B> Pair<A, B>.toLibrary() = APIPair(first, second)
