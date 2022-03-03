package dev.hawu.plugins.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A few utility methods for working with objects.
 *
 * @since 1.5
 */
public final class ObjectUtils {

    private ObjectUtils() {}

    /**
     * Returns the left object only if it is non-null, otherwise
     * returns the right object.
     *
     * @param left  the left object
     * @param right the right object
     * @param <T>   the type of the objects
     * @return the left object if it is non-null, otherwise the right object
     * @since 1.5
     */
    @NotNull
    public static <T> T elvis(final @Nullable T left, final @NotNull T right) {
        return left == null ? right : left;
    }

    /**
     * Attempts to get a string representation of an object
     * despite its nullability.
     *
     * @param object the object to get a string representation for
     * @return the string representation of the object or "null" if the object is null
     * @since 1.5
     */
    @NotNull
    public static String toString(final @Nullable Object object) {
        return object == null ? "null" : object.toString();
    }

}
