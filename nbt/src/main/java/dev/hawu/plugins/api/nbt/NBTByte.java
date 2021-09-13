package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tag holding the value of a signed 8-bit integer.
 *
 * @since 1.0
 */
public final class NBTByte extends NBTType {

    private final byte data;

    /**
     * Constructs a new tag that holds the primitive
     * type byte.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTByte(final byte value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value in the form
     * of a {@link Byte}.
     *
     * @return The contained value.
     * @since 1.0
     */
    public byte getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        return data + "b";
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTByte)) return false;
        return ((NBTByte) other).data == data;
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTByte(this.data);
    }

}
