package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;

/**
 * Represents Bukkit's versions for cross-version support.
 *
 * @since 1.0
 */
public enum MinecraftVersion {

    v1_8_R1,
    v1_8_R2,
    v1_8_R3,
    v1_9_R1,
    v1_9_R2,
    v1_10_R1,
    v1_11_R1,
    v1_12_R1,
    v1_13_R1,
    v1_13_R2,
    v1_14_R1,
    v1_15_R1,
    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_17_R1,
    ;

    private static MinecraftVersion current;

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

}
