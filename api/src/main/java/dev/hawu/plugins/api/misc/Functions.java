package dev.hawu.plugins.api.misc;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A collection of functions that are useful for functional programming.
 */
public final class Functions {

    /**
     * Returns the identity of the given object.
     *
     * @param t   The object to return.
     * @param <T> The type of the object.
     * @return The identity of the given object.
     */
    public static <T> T identity(T t) {
        return t;
    }

    /**
     * Uses an object, acts on it, then returns that object.
     *
     * @param t        The object to use.
     * @param consumer The consumer to act on the object.
     * @param <T>      The type of the object.
     * @return The object.
     */
    public static <T> T with(T t, Consumer<T> consumer) {
        consumer.accept(t);
        return t;
    }

    /**
     * Transforms an object into another object.
     *
     * @param t        The object to transform.
     * @param function The function to transform the object.
     * @param <T>      The type of the object.
     * @param <R>      The type of the transformed object.
     * @return The transformed object.
     */
    public static <T, R> R transform(T t, Function<T, R> function) {
        return function.apply(t);
    }

}
