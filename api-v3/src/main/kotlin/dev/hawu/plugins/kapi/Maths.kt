package dev.hawu.plugins.kapi

import kotlin.math.abs

const val epsilon = 1e-6

/**
 * Returns true if [this] double is close to [other] double.
 */
fun Double.closeEquals(other: Double, diff: Double = epsilon) = abs(this - other) < diff
