package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;

/**
 * An exception thrown when {@link SimpleLookup} functions had trouble
 * or unexpected results during lookups.
 *
 * @since 1.0
 */
public final class LookupException extends RuntimeException {

    /**
     * Default constructor with no arguments.
     *
     * @since 1.0
     */
    public LookupException() {
        super();
    }

    /**
     * Constructs a more detailed exception with a message.
     *
     * @param message The message for the exception.
     * @since 1.0
     */
    public LookupException(@NotNull final String message) {
        super(message);
    }

    /**
     * Constructs an exception from a base exception.
     *
     * @param throwable The exception to initialize with.
     * @since 1.0
     */
    public LookupException(@NotNull final Throwable throwable) {
        super(throwable);
    }

}
