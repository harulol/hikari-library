package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * A tag holding the value of an array of 64-bit integers.
 *
 * @since 1.0
 */
public final class NBTLongArray extends NBTType {

    private final long[] data;

    /**
     * Constructs a new tag that holds the type {@code long[]}.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTLongArray(final long[] value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value.
     *
     * @return The contained value.
     * @since 1.0
     */
    public long[] getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        final StringJoiner joiner = new StringJoiner(",", "[L;", "]");
        Arrays.stream(data).forEach(i -> joiner.add(String.valueOf(i)));
        return joiner.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTLongArray)) return false;
        return Arrays.equals(((NBTLongArray) other).data, data);
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTLongArray(this.data);
    }

}
