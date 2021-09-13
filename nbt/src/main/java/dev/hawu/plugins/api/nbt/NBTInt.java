package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tag holding the value of a signed 32-bit integer.
 *
 * @since 1.0
 */
public final class NBTInt extends NBTType {

    private final int data;

    /**
     * Constructs a new tag that holds the primitive
     * type int.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTInt(final int value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value in the form
     * of a {@link Integer}.
     *
     * @return The contained value.
     * @since 1.0
     */
    public int getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        return String.valueOf(this.data);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTInt)) return false;
        return ((NBTInt) other).data == data;
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTInt(this.data);
    }

}
