package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tag holding the value of a signed 16-bit integer.
 *
 * @since 1.0
 */
public final class NBTShort extends NBTType {

    private final short data;

    /**
     * Constructs a new tag that holds the primitive
     * type short.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTShort(final short value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value in the form
     * of a {@link Short}.
     *
     * @return The contained value.
     * @since 1.0
     */
    public short getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        return data + "s";
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTShort)) return false;
        return ((NBTShort) other).data == data;
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTShort(this.data);
    }

}
