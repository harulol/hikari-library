package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
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

    private UncheckedHandles() {}

    /**
     * Recalls the method {@link Lookup#findConstructor(Class, MethodType)} with
     * the passed parameters and returns the result.
     *
     * @param lookup The {@link Lookup} to use.
     * @param ref    The class to lookup.
     * @param type   The constructor method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findConstructor(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final MethodType type) {
        try {
            return lookup.findConstructor(ref, type);
        } catch(final NoSuchMethodException | IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link Lookup#findGetter(Class, String, Class)} with
     * the passed parameters and returns the result.
     *
     * @param lookup The {@link Lookup} to use.
     * @param ref    The class to lookup.
     * @param name   The name of the field.
     * @param type   The field method type.
     * @return The {@link MethodHandle} or {@code null} if any errors happened.
     * @since 1.0
     */
    @Nullable
    public static MethodHandle findGetter(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return lookup.findGetter(ref, name, type);
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
    public static MethodHandle findSetter(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return lookup.findSetter(ref, name, type);
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
    public static MethodHandle findStaticGetter(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return lookup.findStaticGetter(ref, name, type);
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
    public static MethodHandle findStaticSetter(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return lookup.findStaticSetter(ref, name, type);
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
    public static MethodHandle findStatic(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final MethodType type) {
        try {
            return lookup.findStatic(ref, name, type);
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
    public static MethodHandle findVirtual(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final MethodType type) {
        try {
            return lookup.findVirtual(ref, name, type);
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
    public static MethodHandle findSpecial(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name,
                                           @NotNull final MethodType type, @NotNull final Class<?> specialCaller) {
        try {
            return lookup.findSpecial(ref, name, type, specialCaller);
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
    public static MethodHandle unreflect(final @NotNull Lookup lookup, @NotNull final Method m) {
        try {
            return lookup.unreflect(m);
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
    public static MethodHandle unreflect(final @NotNull Lookup lookup, @NotNull final Constructor<?> c) {
        try {
            return lookup.unreflectConstructor(c);
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
    public static MethodHandle unreflectGetter(final @NotNull Lookup lookup, @NotNull final Field f) {
        try {
            return lookup.unreflectGetter(f);
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
    public static MethodHandle unreflectSetter(final @NotNull Lookup lookup, @NotNull final Field f) {
        try {
            return lookup.unreflectSetter(f);
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
    public static MethodHandle unreflectSpecial(final @NotNull Lookup lookup, @NotNull final Method m, @NotNull Class<?> specialCaller) {
        try {
            return lookup.unreflectSpecial(m, specialCaller);
        } catch(final IllegalAccessException e) {
            return null;
        }
    }

    /**
     * Recalls the method {@link java.lang.invoke.LambdaMetafactory#metafactory(Lookup, String, MethodType, MethodType, MethodHandle, MethodType)}
     * with the passed parameters, then returns the result.
     *
     * @param lookup                 The {@link Lookup} to use.
     * @param invokedName            The name of the invoked method.
     * @param invokedType            The type of the invoked method.
     * @param samMethodType          The type of the SAM method.
     * @param implMethod             The method to invoke.
     * @param instantiatedMethodType The type of the instantiated method.
     * @return The callsite generated by metafactory.
     * @since 1.3
     */
    @Nullable
    public static CallSite metafactory(final @NotNull Lookup lookup, final @NotNull String invokedName, final @NotNull MethodType invokedType,
                                       final @NotNull MethodType samMethodType, final @NotNull MethodHandle implMethod,
                                       final @NotNull MethodType instantiatedMethodType) {
        try {
            return LambdaMetafactory.metafactory(lookup, invokedName, invokedType,
                samMethodType, implMethod, instantiatedMethodType);
        } catch(final Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves an instance to build an invocation
     * to metafactory.
     *
     * @param lookup The {@link Lookup} to use.
     * @return The {@link MetafactoryBuilder} instance.
     * @since 1.3
     */
    @NotNull
    public static MetafactoryBuilder metafactoryBuilder(final @NotNull Lookup lookup) {
        return new MetafactoryBuilder(lookup);
    }

}
