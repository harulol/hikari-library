package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Helper utility class for finding method handles
 * without the need of try-catch clauses because of
 * checked exceptions.
 *
 * @since 1.0
 */
public final class UncheckedHandles {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private UncheckedHandles() {}

    /**
     * Recalls the method {@link Lookup#findConstructor(Class, MethodType)} with
     * the passed parameters and returns the result.
     *
     * @param ref  The class to lookup.
     * @param type The constructor method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findConstructor(@NotNull final Class<?> ref, @NotNull final MethodType type) {
        try {
            return LOOKUP.findConstructor(ref, type);
        } catch(final NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#findGetter(Class, String, Class)} with
     * the passed parameters and returns the result.
     *
     * @param ref  The class to lookup.
     * @param name The name of the field.
     * @param type The field method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findGetter(@NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return LOOKUP.findGetter(ref, name, type);
        } catch(final IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#findSetter(Class, String, Class)} with
     * the passed parameters and returns the result.
     *
     * @param ref  The class to lookup.
     * @param name The name of the field.
     * @param type The field method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findSetter(@NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return LOOKUP.findSetter(ref, name, type);
        } catch(final IllegalAccessException | NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#findStaticGetter(Class, String, Class)} with
     * the passed parameters and returns the result.
     *
     * @param ref  The class to lookup.
     * @param name The name of the field.
     * @param type The field method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findStaticGetter(@NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return LOOKUP.findStaticGetter(ref, name, type);
        } catch(final NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#findStaticSetter(Class, String, Class)} with
     * the passed parameters and returns the result.
     *
     * @param ref  The class to lookup.
     * @param name The name of the field.
     * @param type The field method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findStaticSetter(@NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return LOOKUP.findStaticSetter(ref, name, type);
        } catch(final NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#findStatic(Class, String, MethodType)} with
     * the passed parameters and returns the result.
     *
     * @param ref  The class to lookup.
     * @param name The name of the method.
     * @param type The method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findStatic(@NotNull final Class<?> ref, @NotNull final String name, @NotNull final MethodType type) {
        try {
            return LOOKUP.findStatic(ref, name, type);
        } catch(final NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#findVirtual(Class, String, MethodType)} with
     * the passed parameters and returns the result.
     *
     * @param ref  The class to lookup.
     * @param name The name of the method.
     * @param type The method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findVirtual(@NotNull final Class<?> ref, @NotNull final String name, @NotNull final MethodType type) {
        try {
            return LOOKUP.findVirtual(ref, name, type);
        } catch(final NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#findSpecial(Class, String, MethodType, Class)} with
     * the passed parameters and returns the result.
     *
     * @param ref           The class to lookup.
     * @param name          The name of the method.
     * @param type          The method type.
     * @param specialCaller The proposed calling class to invoke {@code invokespecial}.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findSpecial(@NotNull final Class<?> ref, @NotNull final String name, @NotNull final MethodType type, @NotNull final Class<?> specialCaller) {
        try {
            return LOOKUP.findSpecial(ref, name, type, specialCaller);
        } catch(final NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflect(Method)} with the passed parameters
     * and returns the result.
     *
     * @param m The method to unreflect.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle unreflect(@NotNull final Method m) {
        try {
            return LOOKUP.unreflect(m);
        } catch(final IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflectConstructor(Constructor)} with the passed parameters
     * and returns the result.
     *
     * @param c The constructor to unreflect.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle unreflect(@NotNull final Constructor<?> c) {
        try {
            return LOOKUP.unreflectConstructor(c);
        } catch(final IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflectGetter(Field)} with the passed parameters
     * and returns the result.
     *
     * @param f The field whose getter to unreflect.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle unreflectGetter(@NotNull final Field f) {
        try {
            return LOOKUP.unreflectGetter(f);
        } catch(final IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflectSetter(Field)} with the passed parameters
     * and returns the result.
     *
     * @param f The field whose setter to unreflect.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle unreflectSetter(@NotNull final Field f) {
        try {
            return LOOKUP.unreflectSetter(f);
        } catch(final IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflectSpecial(Method, Class)} with the passed parameters
     * and returns the result.
     *
     * @param m             The method to unreflect.
     * @param specialCaller The class to invoke {@code invokespecial}.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle unreflectSpecial(@NotNull final Method m, @NotNull Class<?> specialCaller) {
        try {
            return LOOKUP.unreflectSpecial(m, specialCaller);
        } catch(final IllegalAccessException e) {
            return null;
        }
    }

}
