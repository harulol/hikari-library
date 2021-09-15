package dev.hawu.plugins.api.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * A non-generic exception that is thrown when an error happens
 * or something fails to work properly while trying to apply
 * a {@link NBTCompound} to an {@link org.bukkit.inventory.ItemStack}.
 *
 * @since 1.0
 */
public final class CompoundApplicationException extends RuntimeException {

    /**
     * Empty constructor for the exception.
     *
     * @since 1.0
     */
    public CompoundApplicationException() {
        super();
    }

    /**
     * Constructs the exception with a custom message.
     *
     * @param message The message to throw with.
     * @since 1.0
     */
    public CompoundApplicationException(@NotNull final String message) {
        super(message);
    }

    /**
     * Constructs the exception from a base throwable.
     *
     * @param throwable The throwable to initialize from.
     * @since 1.0
     */
    public CompoundApplicationException(@NotNull final Throwable throwable) {
        super(throwable);
    }

}
