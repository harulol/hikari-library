package dev.hawu.plugins.api.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The base type of named binary tags. As they are
 * container objects, their referent should always be immutable, but not frozen.
 *
 * @since 1.0
 */
public abstract class NBTType {

    /**
     * Computes the {@link String} representation of this named
     * binary tag.
     *
     * @return The tag serialized to a {@link String}.
     * @since 1.0
     */
    @NotNull
    public abstract String toString();

    /**
     * Computes the hash code of the data contained within this
     * binary tag.
     *
     * @return The computed hash of the data.
     * @since 1.0
     */
    public abstract int hashCode();

    /**
     * Checks if the provided parameter value is equal to
     * the current named tag.
     *
     * @param other The other object to compare to.
     * @return True if other is equal to this, false otherwise.
     * @since 1.0
     */
    public abstract boolean equals(@Nullable Object other);

    /**
     * Clones this tag and retrieves an entirely new tag
     * with the same value of this tag.
     *
     * @return A newly created tag.
     * @since 1.0
     */
    @NotNull
    public abstract NBTType clone();

}
