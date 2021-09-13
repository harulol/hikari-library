package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tag holding the value of a signed 64-bit integer.
 *
 * @since 1.0
 */
public final class NBTLong extends NBTType {

    private final long data;

    /**
     * Constructs a new tag that holds the primitive
     * type long.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTLong(final long value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value in the form
     * of a {@link Long}.
     *
     * @return The contained value.
     * @since 1.0
     */
    public long getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        return data + "l";
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTLong)) return false;
        return ((NBTLong) other).data == data;
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTLong(this.data);
    }

}
