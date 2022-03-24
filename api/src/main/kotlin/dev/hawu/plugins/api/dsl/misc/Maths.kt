package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.MathUtils
import org.bukkit.util.Vector

/**
 * Safely normalizes a vector, so it doesn't throw random
 * NaNs if the vector's length is 0.
 */
fun Vector.safeNormalize() = MathUtils.normalize(this)
