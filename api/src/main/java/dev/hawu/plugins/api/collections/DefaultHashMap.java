package dev.hawu.plugins.api.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a mutable hash map that has a default value,
 * and should never yield {@code null} as a result from
 * {@link DefaultHashMap#get(Object)}.
 *
 * @param <K> The type of the key.
 * @param <V> The type of the value.
 * @since 1.0
 */
public final class DefaultHashMap<K, V> extends HashMap<K, V> {

    private final V defaultValue;

    /**
     * Constructs a default map implementation with an initial capacity and
     * load factor.
     *
     * @param initialCapacity The map's initial capacity.
     * @param loadFactor      The map's load factor.
     * @param defaultValue    The map's default value.
     * @since 1.0
     */
    public DefaultHashMap(final int initialCapacity, final float loadFactor, @NotNull final V defaultValue) {
        super(initialCapacity, loadFactor);
        this.defaultValue = defaultValue;
    }

    /**
     * Constructs a default map implementation with an initial capacity.
     *
     * @param initialCapacity The map's initial capacity.
     * @param defaultValue    The map's default value.
     * @since 1.0
     */
    public DefaultHashMap(final int initialCapacity, @NotNull final V defaultValue) {
        super(initialCapacity);
        this.defaultValue = defaultValue;
    }

    /**
     * Constructs a default map implementation.
     *
     * @param defaultValue The map's default value.
     * @since 1.0
     */
    public DefaultHashMap(@NotNull final V defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Constructs a default map implementation with initial values in the form
     * of a map.
     *
     * @param m            The initial values.
     * @param defaultValue The map's default value.
     * @since 1.0
     */
    public DefaultHashMap(final @NotNull Map<? extends K, ? extends V> m, @NotNull final V defaultValue) {
        super(m);
        this.defaultValue = defaultValue;
    }

    /**
     * Retrieves the value bound to the key. If the key is not
     * present within the map, retrieve the default value
     * instead.
     * <p>
     * This method will never return {@code null}.
     *
     * @param key The key to lookup.
     * @return The value bound to the key, or {@code defaultValue} if not present.
     * @since 1.0
     */
    @NotNull
    @Override
    public V get(@Nullable final Object key) {
        return this.containsKey(key) ? super.get(key) : defaultValue;
    }

}
