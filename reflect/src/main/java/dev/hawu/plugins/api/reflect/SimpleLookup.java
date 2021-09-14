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
     * @throws LookupException If the provided class could not be found.
     * @since 1.0
     */
    @NotNull
    public static Class<?> lookupOBC(@NotNull final String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + MinecraftVersion.getCurrent().name() + "." + name);
        } catch(final ClassNotFoundException ex) {
            throw new LookupException(ex);
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
        } catch(final LookupException ex) {
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
     * @throws LookupException If the class could not be found.
     * @since 1.0
     */
    @NotNull
    @Deprecated
    public static Class<?> lookupNMS(@NotNull final String name) {
        try {
            return MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_17_R1)
                    ? Class.forName("net.minecraft.server." + name)
                    : Class.forName("net.minecraft.server." + MinecraftVersion.getCurrent().name() + "." + name);
        } catch(final ClassNotFoundException ex) {
            throw new LookupException(ex);
        }
    }

    /**
     * Looks up a class within the package {@code net.minecraft}.
     * The path will be implicitly appended by the library.
     * <p>
     * This method will check for the current Minecraft version, and if the version
     * is at least {@code 1.17}, the path to look for would be {@code net.minecraft.<newPath>}.
     * <p>
     * And if the server is running a version lower than 1.17, when packages' names
     * still have the version tags, the path to look for the class would be
     * {@code net.minecraft.server.VERSION.<legacyPath>}.
     *
     * @param legacyPath The path to look for if the Bukkit version is below {@code 1.17}.
     * @param newPath    The path to look for if the Bukkit version is at least {@code 1.17}.
     * @return A class instance if it is found.
     * @throws LookupException If the class could not be resolved.
     * @since 1.1
     */
    @NotNull
    public static Class<?> lookupNMS(@NotNull final String legacyPath, @NotNull final String newPath) {
        try {
            return MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_17_R1)
                    ? Class.forName("net.minecraft." + newPath)
                    : Class.forName("net.minecraft.server." + MinecraftVersion.v1_17_R1.name() + "." + legacyPath);
        } catch(final ClassNotFoundException ex) {
            throw new LookupException(ex);
        }
    }

    /**
     * Makes an attempt to look up a class within the packages {@code net.minecraft.server}.
     * The version tag should be appended implicitly by the library if required.
     * <p>
     * This is a shorthand for using:
     * <pre>
     * {@code
     *     Class<?> nmsClass;
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
    @Deprecated
    public static Class<?> lookupNMSOrNull(@NotNull final String name) {
        try {
            return lookupNMS(name);
        } catch(final LookupException ex) {
            return null;
        }
    }

    /**
     * Makes an attempt to look for a class in the package {@code net.minecraft}.
     * The path will be implicitly appended by the library.
     * <p>
     * Shorthand for:
     * <pre>
     * {@code
     *     Class<?> nmsClass;
     *     try {
     *         nmsClass = lookupNMS(legacyPath, newPath);
     *     } catch(final LookupException ex) {
     *         nmsClass = null;
     *     }
     * }
     * </pre>
     *
     * @param legacyPath The path to append when the version is below {@code 1.17}.
     * @param newPath    The path to append when the version is at least {@code 1.17}.
     * @return The {@link Class} instance if found, {@code null} otherwise.
     * @since 1.1
     */
    public static Class<?> lookupNMSOrNull(@NotNull final String legacyPath, @NotNull final String newPath) {
        try {
            return lookupNMS(legacyPath, newPath);
        } catch(final LookupException ex) {
            return null;
        }
    }

}
