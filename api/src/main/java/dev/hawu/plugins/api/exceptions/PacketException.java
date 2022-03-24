package dev.hawu.plugins.api.exceptions;

/**
 * Exceptions thrown when there are unexpected errors related
 * to packets sending. Usually happens when the server runs a modified
 * version of Bukkit.
 *
 * @since 1.6
 */
public final class PacketException extends RuntimeException {

    /**
     * New packet exception.
     */
    public PacketException() {
        super();
    }

    /**
     * New packet exception.
     *
     * @param message the exception message
     * @since 1.6
     */
    public PacketException(String message) {
        super(message);
    }

    /**
     * New packet exception.
     *
     * @param cause the underlying cause
     * @since 1.6
     */
    public PacketException(Throwable cause) {
        super(cause);
    }

}
