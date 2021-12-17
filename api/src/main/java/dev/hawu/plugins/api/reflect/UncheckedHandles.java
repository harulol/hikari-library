package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

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
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> findConstructor(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final MethodType type) {
        try {
            return Optional.of(lookup.findConstructor(ref, type));
        } catch(final Exception e) {
            return Optional.empty();
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
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> findGetter(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return Optional.of(lookup.findGetter(ref, name, type));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#findSetter(Class, String, Class)} with
     * the passed parameters and returns the result.
     *
     * @param lookup The lookup to use
     * @param ref    The class to lookup.
     * @param name   The name of the field.
     * @param type   The field method type.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> findSetter(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return Optional.of(lookup.findSetter(ref, name, type));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#findStaticGetter(Class, String, Class)} with
     * the passed parameters and returns the result.
     *
     * @param lookup The lookup to use
     * @param ref    The class to lookup.
     * @param name   The name of the field.
     * @param type   The field method type.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> findStaticGetter(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return Optional.of(lookup.findStaticGetter(ref, name, type));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#findStaticSetter(Class, String, Class)} with
     * the passed parameters and returns the result.
     *
     * @param lookup The lookup to use
     * @param ref    The class to lookup.
     * @param name   The name of the field.
     * @param type   The field method type.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> findStaticSetter(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final Class<?> type) {
        try {
            return Optional.of(lookup.findStaticSetter(ref, name, type));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#findStatic(Class, String, MethodType)} with
     * the passed parameters and returns the result.
     *
     * @param lookup The lookup to use
     * @param ref    The class to lookup.
     * @param name   The name of the method.
     * @param type   The method type.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> findStatic(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final MethodType type) {
        try {
            return Optional.of(lookup.findStatic(ref, name, type));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#findVirtual(Class, String, MethodType)} with
     * the passed parameters and returns the result.
     *
     * @param lookup The lookup to use
     * @param ref    The class to lookup.
     * @param name   The name of the method.
     * @param type   The method type.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> findVirtual(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name, @NotNull final MethodType type) {
        try {
            return Optional.of(lookup.findVirtual(ref, name, type));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#findSpecial(Class, String, MethodType, Class)} with
     * the passed parameters and returns the result.
     *
     * @param lookup        The lookup to use
     * @param ref           The class to lookup.
     * @param name          The name of the method.
     * @param type          The method type.
     * @param specialCaller The proposed calling class to invoke {@code invokespecial}.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> findSpecial(final @NotNull Lookup lookup, @NotNull final Class<?> ref, @NotNull final String name,
                                                     @NotNull final MethodType type, @NotNull final Class<?> specialCaller) {
        try {
            return Optional.of(lookup.findSpecial(ref, name, type, specialCaller));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflect(Method)} with the passed parameters
     * and returns the result.
     *
     * @param lookup The lookup to use
     * @param m      The method to unreflect.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> unreflect(final @NotNull Lookup lookup, @NotNull final Method m) {
        try {
            return Optional.of(lookup.unreflect(m));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflectConstructor(Constructor)} with the passed parameters
     * and returns the result.
     *
     * @param lookup The lookup to use
     * @param c      The constructor to unreflect.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> unreflect(final @NotNull Lookup lookup, @NotNull final Constructor<?> c) {
        try {
            return Optional.of(lookup.unreflectConstructor(c));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflectGetter(Field)} with the passed parameters
     * and returns the result.
     *
     * @param lookup The lookup to use
     * @param f      The field whose getter to unreflect.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> unreflectGetter(final @NotNull Lookup lookup, @NotNull final Field f) {
        try {
            return Optional.of(lookup.unreflectGetter(f));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflectSetter(Field)} with the passed parameters
     * and returns the result.
     *
     * @param lookup The lookup to use
     * @param f      The field whose setter to unreflect.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> unreflectSetter(final @NotNull Lookup lookup, @NotNull final Field f) {
        try {
            return Optional.of(lookup.unreflectSetter(f));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Recalls the method {@link Lookup#unreflectSpecial(Method, Class)} with the passed parameters
     * and returns the result.
     *
     * @param lookup        The lookup to use
     * @param m             The method to unreflect.
     * @param specialCaller The class to invoke {@code invokespecial}.
     * @return The {@link MethodHandle} or {@code null} inside an {@link Optional} if any errors happened.
     * @since 1.0
     */
    @NotNull
    public static Optional<MethodHandle> unreflectSpecial(final @NotNull Lookup lookup, @NotNull final Method m, @NotNull Class<?> specialCaller) {
        try {
            return Optional.of(lookup.unreflectSpecial(m, specialCaller));
        } catch(final Exception e) {
            return Optional.empty();
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
     * @return The callsite generated by metafactory wrapped inside an {@link Optional}.
     * @since 1.3
     */
    @NotNull
    public static Optional<CallSite> metafactory(final @NotNull Lookup lookup, final @NotNull String invokedName, final @NotNull MethodType invokedType,
                                                 final @NotNull MethodType samMethodType, final @NotNull MethodHandle implMethod,
                                                 final @NotNull MethodType instantiatedMethodType) {
        try {
            return Optional.of(LambdaMetafactory.metafactory(lookup, invokedName, invokedType,
                samMethodType, implMethod, instantiatedMethodType));
        } catch(final Throwable e) {
            return Optional.empty();
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
