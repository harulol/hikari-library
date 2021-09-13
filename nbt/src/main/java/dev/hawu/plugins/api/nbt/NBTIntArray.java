package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * A tag holding the value of an array of 16-bit integers.
 *
 * @since 1.0
 */
public final class NBTIntArray extends NBTType {

    private final int[] data;

    /**
     * Constructs a new tag that holds the type {@code int[]}.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTIntArray(final int[] value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value.
     *
     * @return The contained value.
     * @since 1.0
     */
    public int[] getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        final StringJoiner joiner = new StringJoiner(",", "[I;", "]");
        Arrays.stream(data).forEach(i -> joiner.add(String.valueOf(i)));
        return joiner.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTIntArray)) return false;
        return Arrays.equals(((NBTIntArray) other).data, data);
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTIntArray(this.data);
    }

}
