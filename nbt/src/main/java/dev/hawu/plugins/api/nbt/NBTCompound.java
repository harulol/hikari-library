package dev.hawu.plugins.api.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a compound tag that holds an underlying
 * map of {@link String} and {@link NBTType}.
 *
 * @since 1.0
 */
public final class NBTCompound extends NBTType implements Map<@NotNull String, @NotNull NBTType> {

    private final Map<String, NBTType> map;

    /**
     * Constructs a new compound with an empty hash map.
     *
     * @since 1.0
     */
    public NBTCompound() {
        this.map = new HashMap<>();
    }

    /**
     * Constructs a new compound with the values already present
     * within the provided map.
     *
     * @param map The initial values.
     * @since 1.0
     */
    public NBTCompound(@NotNull final Map<@NotNull String, @NotNull NBTType> map) {
        this.map = map;
    }

    @Override
    public @NotNull String toString() {
        return map.toString();
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if(!(other instanceof NBTCompound)) return false;
        return map.equals(((NBTCompound) other).map);
    }

    @Override
    public @NotNull NBTType clone() {
        return new NBTCompound(new HashMap<>(this.map));
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(@Nullable final Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(@Nullable final Object value) {
        return map.containsValue(value);
    }

    @Override
    @Nullable
    public NBTType get(@Nullable final Object key) {
        return map.get(key);
    }

    @Override
    public @Nullable NBTType put(@NotNull final String key, @NotNull final NBTType value) {
        return map.put(key, value);
    }

    @Override
    public @Nullable NBTType remove(final Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(@NotNull final Map<? extends @NotNull String, ? extends @NotNull NBTType> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @NotNull
    @Override
    public Set<@NotNull String> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<@NotNull NBTType> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<@NotNull String, @NotNull NBTType>> entrySet() {
        return map.entrySet();
    }

    @Override
    public @NotNull NBTType getOrDefault(@Nullable final Object key, @NotNull final NBTType defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(final BiConsumer<? super @NotNull String, ? super @NotNull NBTType> action) {
        Map.super.forEach(action);
    }

    @Override
    public void replaceAll(final BiFunction<? super @NotNull String, ? super @NotNull NBTType, ? extends @NotNull NBTType> function) {
        Map.super.replaceAll(function);
    }

    @Nullable
    @Override
    public NBTType putIfAbsent(@NotNull final String key, @NotNull final NBTType value) {
        return Map.super.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(@Nullable final Object key, @Nullable final Object value) {
        return Map.super.remove(key, value);
    }

    @Override
    public boolean replace(@NotNull final String key, @NotNull final NBTType oldValue, @NotNull final NBTType newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    @Nullable
    @Override
    public NBTType replace(@NotNull final String key, @NotNull final NBTType value) {
        return Map.super.replace(key, value);
    }

    @Override
    public @NotNull NBTType computeIfAbsent(@NotNull final String key, @NotNull final Function<? super @NotNull String, ? extends @NotNull NBTType> mappingFunction) {
        return Map.super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    @Nullable
    public NBTType computeIfPresent(@NotNull final String key,
                                    @NotNull final BiFunction<? super @NotNull String, ? super @NotNull NBTType, ? extends @NotNull NBTType> remappingFunction) {
        return Map.super.computeIfPresent(key, remappingFunction);
    }

    @Override
    public @NotNull NBTType compute(@NotNull final String key,
                                    @NotNull final BiFunction<? super @NotNull String, ? super @NotNull NBTType, ? extends @NotNull NBTType> remappingFunction) {
        return Map.super.compute(key, remappingFunction);
    }

    @Override
    public @NotNull NBTType merge(@NotNull final String key, @NotNull final NBTType value,
                                  @NotNull final BiFunction<? super @NotNull NBTType, ? super @NotNull NBTType, ? extends @NotNull NBTType> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }

}
