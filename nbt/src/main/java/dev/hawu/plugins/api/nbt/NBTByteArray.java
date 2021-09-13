package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * A tag holding the value of an array of 8-bit integers.
 *
 * @since 1.0
 */
public final class NBTByteArray extends NBTType {

    private final byte[] data;

    /**
     * Constructs a new tag that holds the type {@code byte[]}.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTByteArray(final byte[] value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value.
     *
     * @return The contained value.
     * @since 1.0
     */
    public byte[] getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        final StringJoiner joiner = new StringJoiner(",", "[B;", "]");
        for(final byte b : data) {
            joiner.add(String.valueOf(b));
        }
        return joiner.toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTByteArray)) return false;
        return Arrays.equals(((NBTByteArray) other).data, data);
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTByteArray(this.data);
    }

}
