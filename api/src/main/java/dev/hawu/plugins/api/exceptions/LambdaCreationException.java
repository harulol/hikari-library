package dev.hawu.plugins.api.exceptions;

/**
 * An exception specifically thrown when a lambda callsite
 * failed to create.
 *
 * @since 2.0
 */
public final class LambdaCreationException extends RuntimeException {

    /**
     * Creates a new empty lambda creation exception.
     *
     * @since 2.0
     */
    public LambdaCreationException() {
    }

    /**
     * Constructs a new lambda creation exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @since 2.0
     */
    public LambdaCreationException(final String message) {
        super(message);
    }

    /**
     * Constructs a new lambda creation exception with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of
     * {@code cause}).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A {@code null} value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 2.0
     */
    public LambdaCreationException(final Throwable cause) {
        super(cause);
    }

}
