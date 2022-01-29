package dev.hawu.plugins.api.exceptions;

import org.jetbrains.annotations.NotNull;

/**
 * An exception that is thrown whenever a re-assignment is attempted
 * on a field that is already assigned using safe-guarded methods.
 *
 * @since 1.5
 */
public final class AlreadyInitializedException extends RuntimeException {

    /**
     * Constructs a basic exception with {@code null} as the message.
     *
     * @since 1.5
     */
    public AlreadyInitializedException() {
        super();
    }

    /**
     * Constructs a basic exception with a custom message.
     *
     * @param msg The message for the exception.
     * @since 1.5
     */
    public AlreadyInitializedException(final @NotNull String msg) {
        super(msg);
    }

    /**
     * Constructs a basic exception using a base throwable.
     *
     * @param throwable The base throwable.
     * @since 1.5
     */
    public AlreadyInitializedException(final @NotNull Throwable throwable) {
        super(throwable);
    }

}
