package dev.hawu.plugins.api;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Utils related to mathematics :P
 *
 * @since 1.5
 */
public final class MathUtils {

    /**
     * Calculates the log base n of a number.
     *
     * @param base  the log base
     * @param value the value
     * @return the log base n of a number
     * @since 1.5
     */
    public static double log(final double base, final double value) {
        return Math.log(value) / Math.log(base);
    }

    /**
     * Normalizes a vector while taking into account that it
     * could be a zero vector.
     *
     * @param vector the vector to normalize
     * @return the normalized vector
     * @since 1.5
     */
    @NotNull
    public static Vector normalize(final @NotNull Vector vector) {
        if(vector.lengthSquared() == 0) return vector;
        else return vector.normalize();
    }

    /**
     * Rotates the provided vector around the X axis.
     *
     * @param v     the vector to rotate
     * @param angle the angle to rotate by (in degrees)
     * @return the rotated vector
     * @since 1.6
     */
    @NotNull
    public static Vector rotateAroundAxisX(final @NotNull Vector v, final double angle) {
        final double radians = Math.toRadians(angle);
        return rotateAroundAxisX(v, Math.cos(radians), Math.sin(radians));
    }

    /**
     * Rotates the provided vector around the Y axis.
     *
     * @param v     the vector to rotate
     * @param angle the angle to rotate by (in degrees)
     * @return the rotated vector
     * @since 1.6
     */
    @NotNull
    public static Vector rotateAroundAxisY(final @NotNull Vector v, final double angle) {
        final double radians = Math.toRadians(-angle);
        return rotateAroundAxisY(v, Math.cos(radians), Math.sin(radians));
    }

    /**
     * Rotates the provided vector around the Z axis.
     *
     * @param v     the vector to rotate
     * @param angle the angle to rotate by (in degrees)
     * @return the rotated vector
     * @since 1.6
     */
    @NotNull
    public static Vector rotateAroundAxisZ(final @NotNull Vector v, final double angle) {
        final double radians = Math.toRadians(angle);
        return rotateAroundAxisZ(v, Math.cos(radians), Math.sin(radians));
    }

    /**
     * Rotates the provided vector around the X axis,
     * with trig functions pre-calculated. Usually for performance reasons.
     *
     * @param v   the vector to rotate
     * @param cos the cosine of the angle to rotate by
     * @param sin the sine of the angle to rotate by
     * @return the rotated vector
     * @author finnbon
     * @since 1.6
     */
    @NotNull
    public static Vector rotateAroundAxisX(final @NotNull Vector v, final double cos, final double sin) {
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    /**
     * Rotates the provided vector around the Y axis,
     * with trig functions pre-calculated. Usually for performance reasons.
     *
     * @param v   the vector to rotate
     * @param cos the cosine of the angle to rotate by
     * @param sin the sine of the angle to rotate by
     * @return the rotated vector
     * @author finnbon
     * @since 1.6
     */
    @NotNull
    public static Vector rotateAroundAxisY(final @NotNull Vector v, final double cos, final double sin) {
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    /**
     * Rotates the provided vector around the Z axis,
     * with trig functions pre-calculated. Usually for performance reasons.
     *
     * @param v   the vector to rotate
     * @param cos the cosine of the angle to rotate by
     * @param sin the sine of the angle to rotate by
     * @return the rotated vector
     * @author finnbon
     * @since 1.6
     */
    @NotNull
    public static Vector rotateAroundAxisZ(final @NotNull Vector v, final double cos, final double sin) {
        double x = v.getX() * cos - v.getY() * sin;
        double y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }

    /**
     * Gets the perpendicular part of vector u on vector onto.
     * This does not mutate the original vectors.
     *
     * @param u    the vector to find the perpendicular part of
     * @param onto the vector to find the perpendicular part on
     * @return the perpendicular part of vector u on vector onto.
     * @author SHUTUPANDUSECAPS
     * @since 1.6
     */
    @NotNull
    public static Vector perpendicular(final @NotNull Vector u, final @NotNull Vector onto) {
        return u.clone().subtract(projection(onto, u));
    }

    /**
     * Gets the projection of vector u on vector onto.
     * This does not mutate the original vectors.
     *
     * @param u    the vector to find the projection of
     * @param onto the vector to find the projection on
     * @return the projection of vector u on vector onto.
     * @author SHUTUPANDUSECAPS
     * @since 1.6
     */
    @NotNull
    public static Vector projection(final @NotNull Vector onto, final @NotNull Vector u) {
        return onto.clone().multiply(onto.dot(u) / onto.lengthSquared());
    }

}
