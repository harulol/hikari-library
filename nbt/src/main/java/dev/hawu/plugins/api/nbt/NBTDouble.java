package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tag holding the value of a signed 64-bit floating point number.
 *
 * @since 1.0
 */
public final class NBTDouble extends NBTType {

    private final double data;

    /**
     * Constructs a new tag that holds the primitive
     * type double.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTDouble(final double value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value in the form
     * of a {@link Double}.
     *
     * @return The contained value.
     * @since 1.0
     */
    public double getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        return String.valueOf(data);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTDouble)) return false;
        return ((NBTDouble) other).data == data;
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTDouble(this.data);
    }

}
