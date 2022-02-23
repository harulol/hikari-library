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

}
