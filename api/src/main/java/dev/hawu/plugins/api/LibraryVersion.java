package dev.hawu.plugins.api;

/**
 * Singleton object for dealing with incompatible
 * library versions.
 *
 * @since 1.1
 */
public final class LibraryVersion {

    private static final LibraryVersion CURRENT = new LibraryVersion(1, 1, 5);

    private final int major;
    private final int minor;
    private final int patch;

    private LibraryVersion(final int major, final int minor, final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Retrieve the current version of the library.
     *
     * @return The current version.
     * @since 1.1
     */
    public static LibraryVersion getCurrent() {
        return CURRENT;
    }

    /**
     * The major number of the semantic version tag.
     *
     * @return The major number.
     * @since 1.1
     */
    public int getMajor() {
        return major;
    }

    /**
     * The minor number of the semantic version tag.
     *
     * @return The minor number.
     * @since 1.1
     */
    public int getMinor() {
        return minor;
    }

    /**
     * The patch number of the semantic version tag.
     *
     * @return The patch number.
     * @since 1.1
     */
    public int getPatch() {
        return patch;
    }

}
