package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tag holding the value of a signed 32-bit floating point number.
 *
 * @since 1.0
 */
public final class NBTFloat extends NBTType {

    private final float data;

    /**
     * Constructs a new tag that holds the primitive
     * type float.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTFloat(final float value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value in the form
     * of a {@link Float}.
     *
     * @return The contained value.
     * @since 1.0
     */
    public float getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        return data + "f";
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTFloat)) return false;
        return ((NBTFloat) other).data == data;
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTFloat(this.data);
    }

}
