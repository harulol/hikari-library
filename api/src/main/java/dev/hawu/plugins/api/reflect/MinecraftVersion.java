package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;

/**
 * Represents Bukkit's versions for cross-version support.
 *
 * @since 1.0
 */
public enum MinecraftVersion {

    /**
     * Represents Minecraft 1.8 to 1.8.2.
     *
     * @since 1.0
     */
    v1_8_R1(1, 8, 0),

    /**
     * Represents Minecraft 1.8.3.
     *
     * @since 1.0
     */
    v1_8_R2(1, 8, 3),

    /**
     * Represents Minecraft 1.8.4 to 1.8.9.
     *
     * @since 1.0
     */
    v1_8_R3(1, 8, 4),

    /**
     * Represents Minecraft 1.9 to 1.9.1.
     *
     * @since 1.0
     */
    v1_9_R1(1, 9, 0),

    /**
     * Represents Minecraft 1.9.2.
     *
     * @since 1.0
     */
    v1_9_R2(1, 9, 2),

    /**
     * Represents Minecraft 1.10 to 1.10.2.
     *
     * @since 1.0
     */
    v1_10_R1(1, 10, 0),

    /**
     * Represents Minecraft 1.11 to 1.11.2.
     *
     * @since 1.0
     */
    v1_11_R1(1, 11, 0),

    /**
     * Represents Minecraft 1.12 to 1.12.2.
     *
     * @since 1.0
     */
    v1_12_R1(1, 12, 0),

    /**
     * Represents Minecraft 1.13 to 1.13.1.
     *
     * @since 1.0
     */
    v1_13_R1(1, 13, 0),

    /**
     * Represents Minecraft 1.13.2.
     *
     * @since 1.0
     */
    v1_13_R2(1, 13, 2),

    /**
     * Represents Minecraft 1.14 to 1.14.4.
     *
     * @since 1.0
     */
    v1_14_R1(1, 14, 0),

    /**
     * Represents Minecraft 1.15 to 1.15.2.
     *
     * @since 1.0
     */
    v1_15_R1(1, 15, 0),

    /**
     * Represents Minecraft 1.16.1.
     *
     * @since 1.0
     */
    v1_16_R1(1, 16, 1),

    /**
     * Represents Minecraft 1.16.2 to 1.16.3.
     *
     * @since 1.0
     */
    v1_16_R2(1, 16, 2),

    /**
     * Represents Minecraft 1.16.4 to 1.16.5.
     *
     * @since 1.0
     */
    v1_16_R3(1, 16, 4),

    /**
     * Represents Minecraft 1.17 to 1.17.1.
     *
     * @since 1.0
     */
    v1_17_R1(1, 17, 0),

    /**
     * Represents Minecraft 1.18.
     *
     * @since 1.3
     */
    v1_18_R1(1, 18, 0),
    ;

    private static MinecraftVersion current;

    private final int major;
    private final int minor;
    private final int patch;

    MinecraftVersion(final int major, final int minor, final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Retrieves the computed version of the current Craftbukkit core.
     *
     * @return A should-be non-null instance of {@link MinecraftVersion}.
     * @since 1.0
     */
    @NotNull
    public static MinecraftVersion getCurrent() {
        return current == null ? (current = MinecraftVersion.valueOf(SimpleLookup.getBukkitVersion())) : current;
    }

    /**
     * Checks if the receiver enum is at least a version after the
     * specified parameter value.
     *
     * @param other The value to check against.
     * @return True if the receiver's version is later than or equal to the parameter, false otherwise.
     * @since 1.0
     */
    public final boolean isAtLeast(@NotNull final MinecraftVersion other) {
        return ordinal() >= other.ordinal();
    }

    /**
     * Gets the major version of this instance.
     *
     * @return The major number in the semantic tag.
     * @since 1.0
     */
    public final int getMajor() {
        return major;
    }

    /**
     * Gets the minor version of this instance.
     *
     * @return The minor number in the semantic tag.
     * @since 1.0
     */
    public final int getMinor() {
        return minor;
    }

    /**
     * Gets the patch version of this instance.
     *
     * @return The patch number in the semantic tag.
     * @since 1.0
     */
    public final int getPatch() {
        return patch;
    }

    /**
     * Checks if the current version is at least later than or equal to
     * the provided semantic version.
     *
     * @param major The major number.
     * @param minor The minor number.
     * @param patch The patch number.
     * @return True if it is, false otherwise.
     * @since 1.0
     */
    public final boolean isAtLeast(final int major, final int minor, final int patch) {
        if(this.major > major) return true;
        return this.major == major && (this.minor == minor ? this.patch >= patch : this.minor > minor);
    }

}
