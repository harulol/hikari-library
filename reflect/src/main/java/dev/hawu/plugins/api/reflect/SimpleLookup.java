package dev.hawu.plugins.api.reflect;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simple utility class to provide very simple and primitive
 * lookups of {@code org.bukkit.craftbukkit} classes and
 * {@code net.minecraft.server} classes.
 *
 * @since 1.0
 */
public final class SimpleLookup {

    private static String bukkitVersion;

    private SimpleLookup() {}

    /**
     * Computes the current Bukkit's version, if not already computed,
     * then retrieves the value as a {@link String}.
     *
     * @return The current running Bukkit's version.
     * @since 1.0
     */
    @NotNull
    public static String getBukkitVersion() {
        if(bukkitVersion == null) {
            final String packageName = Bukkit.getServer().getClass().getPackage().getName();
            return bukkitVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
        }
        return bukkitVersion;
    }

    /**
     * Looks up a class within the packages {@code org.bukkit.craftbukkit}.
     * The version tag is appended implicitly by the library.
     *
     * @param name The name of the class to look for.
     * @return Always a non-null {@link Class} instance.
     * @throws RuntimeException If the provided class could not be found.
     * @since 1.0
     */
    @NotNull
    public static Class<?> lookupOBC(@NotNull final String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + MinecraftVersion.getCurrent().name() + "." + name);
        } catch(final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Makes an attempt to look up a class within the {@code org.bukkit.craftbukkit}
     * packages. The version tag is appended implicitly by the library.
     *
     * @param name The name of the class to look for.
     * @return A {@link Class} instance if the class was found, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public static Class<?> lookupOBCOrNull(@NotNull final String name) {
        try {
            return lookupOBC(name);
        } catch(final RuntimeException ex) {
            return null;
        }
    }

    /**
     * Looks up a class within the packages {@code net.minecraft.server}.
     * The version tag should be appended implicitly by the library.
     * <p>
     * Note that from 1.17, official Minecraft packages dropped the version tag
     * in their package names and moved classes around. It's advised that implementations
     * should check the {@link MinecraftVersion} before attempting to use this.
     * <p>
     * If the currently running version is below 1.17, the class's name to be looked up
     * with be {@code net.minecraft.server.VERSION.name}, or {@code net.minecraft.server.name} otherwise.
     *
     * @param name The name of the class to look up.
     * @return A {@link Class} instance if the class was found.
     * @throws RuntimeException If the class could not be found.
     * @since 1.0
     */
    @NotNull
    public static Class<?> lookupNMS(@NotNull final String name) {
        try {
            return MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_17_R1)
                    ? Class.forName("net.minecraft.server." + name)
                    : Class.forName("net.minecraft.server." + MinecraftVersion.getCurrent().name() + "." + name);
        } catch(final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Makes an attempt to look up a class within the packages {@code net.minecraft.server}.
     * The version tag should be appended implicitly by the library if required.
     * <p>
     * This is a shorthand for using:
     * <pre>
     * {@code
     *     final Class<?>; nmsClass;
     *     try {
     *         nmsClass = lookupNMS(name);
     *     } catch(final RuntimeException ex) {
     *         nmsClass = null;
     *     }
     * }
     * </pre>
     *
     * @param name The name of the class to look up.
     * @return A {@link Class} instance if found, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public static Class<?> lookupNMSOrNull(@NotNull final String name) {
        try {
            return lookupNMS(name);
        } catch(final RuntimeException ex) {
            return null;
        }
    }

}
