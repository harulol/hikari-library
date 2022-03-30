@file:Suppress("UNUSED")

package dev.hawu.plugins.api.dsl.misc

import dev.hawu.plugins.api.MathUtils
import org.bukkit.util.Vector

/**
 * Safely normalizes a vector, so it doesn't throw random
 * NaNs if the vector's length is 0.
 */
fun Vector.safeNormalize() = MathUtils.normalize(this)

/**
 * Rotates a vector around the X axis with the angle
 * in degrees.
 */
fun Vector.rotateX(degree: Double) = MathUtils.rotateAroundAxisX(this, degree)

/**
 * Rotates a vector around the Y axis with the angle
 * in degrees.
 */
fun Vector.rotateY(degree: Double) = MathUtils.rotateAroundAxisY(this, degree)

/**
 * Rotates a vector around the Z axis with the angle
 * in degrees.
 */
fun Vector.rotateZ(degree: Double) = MathUtils.rotateAroundAxisZ(this, degree)

/**
 * Rotates a vector around the X axis with precomputed trig.
 */
fun Vector.rotateX(cos: Double, sin: Double) = MathUtils.rotateAroundAxisX(this, cos, sin)

/**
 * Rotates a vector around the Y axis with precomputed trig.
 */
fun Vector.rotateY(cos: Double, sin: Double) = MathUtils.rotateAroundAxisY(this, cos, sin)

/**
 * Rotates a vector around the Z axis with precomputed trig.
 */
fun Vector.rotateZ(cos: Double, sin: Double) = MathUtils.rotateAroundAxisZ(this, cos, sin)

/**
 * Finds the perpendicular part of [this] onto [other].
 */
fun Vector.perpendicular(other: Vector) = MathUtils.perpendicular(this, other)

/**
 * Finds the projection of [this] onto [other].
 */
fun Vector.project(other: Vector) = MathUtils.projection(this, other)
