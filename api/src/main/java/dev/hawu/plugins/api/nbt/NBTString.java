package dev.hawu.plugins.api.nbt;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A tag holding the value of a string.
 *
 * @since 1.0
 */
public final class NBTString extends NBTType {

    private final String data;

    /**
     * Constructs a new tag that holds the type {@link String}.
     *
     * @param value The value of the tag.
     * @since 1.0
     */
    public NBTString(@NotNull final String value) {
        this.data = value;
    }

    /**
     * Retrieves the contained value.
     *
     * @return The contained value.
     * @since 1.0
     */
    @NotNull
    public String getData() {
        return this.data;
    }

    @Override
    @NotNull
    public String toString() {
        return data;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).toHashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if(!(other instanceof NBTString)) return false;
        return ((NBTString) other).data.equals(data);
    }

    @Override
    @NotNull
    public NBTType clone() {
        return new NBTString(this.data);
    }

}
