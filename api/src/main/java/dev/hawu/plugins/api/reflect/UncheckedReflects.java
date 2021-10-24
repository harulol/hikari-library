package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A utility helper class for finding constructors, methods
 * and fields from classes reflectively without the need
 * of {@code try-catch} blocks due to checked exceptions.
 *
 * @since 1.0
 */
public final class UncheckedReflects {

    /**
     * Attempts to retrieve a {@link Constructor}.
     *
     * @param cls      The class that has the constructor.
     * @param declared Whether the constructor is declared.
     * @param types    The parameter types for the constructor.
     * @return A {@link Constructor} if found, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public static Constructor<?> getConstructor(final @NotNull Class<?> cls, final boolean declared, final @NotNull Class<?> @NotNull ... types) {
        try {
            if(declared)
                return cls.getDeclaredConstructor(types);
            else return cls.getConstructor(types);
        } catch(final NoSuchMethodException ex) {
            return null;
        }
    }

    /**
     * Attempts to retrieve a {@link Method}.
     *
     * @param cls      The class that has the method.
     * @param declared Whether the method is declared.
     * @param name     The name of the method.
     * @param params   The parameter types of the method.
     * @return A {@link Method} if found, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public static Method getMethod(final @NotNull Class<?> cls, final boolean declared, final @NotNull String name, final @NotNull Class<?> @NotNull ... params) {
        try {
            if(declared)
                return cls.getDeclaredMethod(name, params);
            else return cls.getMethod(name, params);
        } catch(final NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Attempts to retrieve a {@link Field}.
     *
     * @param cls      The class that has the field.
     * @param declared Whether the field is declared by cls.
     * @param name     The name of the field.
     * @return A {@link Field} if found, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public static Field getField(final @NotNull Class<?> cls, final boolean declared, final @NotNull String name) {
        try {
            if(declared)
                return cls.getDeclaredField(name);
            else return cls.getField(name);
        } catch(final NoSuchFieldException ex) {
            return null;
        }
    }

    /**
     * Attempts to retrieve a {@link Constructor}.
     *
     * @param cls      The class that has the constructor.
     * @param declared Whether the constructor is declared.
     * @param types    The parameter types for the constructor.
     * @return A {@link Constructor} if found.
     * @throws LookupException If the constructor is not found.
     * @since 1.0
     */
    @NotNull
    public static Constructor<?> getConstructorOrThrow(final @NotNull Class<?> cls, final boolean declared, final @NotNull Class<?> @NotNull ... types) {
        try {
            if(declared)
                return cls.getDeclaredConstructor(types);
            else return cls.getConstructor(types);
        } catch(final NoSuchMethodException ex) {
            throw new LookupException(ex);
        }
    }

    /**
     * Attempts to retrieve a {@link Method}.
     *
     * @param cls      The class that has the method.
     * @param declared Whether the method is declared.
     * @param name     The name of the method.
     * @param params   The parameter types for the method.
     * @return A {@link Method} if found.
     * @throws LookupException If the method is not found.
     * @since 1.0
     */
    @NotNull
    public static Method getMethodOrThrow(final @NotNull Class<?> cls, final boolean declared, final @NotNull String name, final @NotNull Class<?> @NotNull ... params) {
        try {
            if(declared)
                return cls.getDeclaredMethod(name, params);
            else return cls.getMethod(name, params);
        } catch(final NoSuchMethodException e) {
            throw new LookupException(e);
        }
    }

    /**
     * Attempts to retrieve a {@link Field}.
     *
     * @param cls      The class that has the field.
     * @param declared Whether the field is declared by cls.
     * @param name     The name of the field.
     * @return A {@link Field} if found.
     * @throws LookupException If the method is not found.
     * @since 1.0
     */
    @NotNull
    public static Field getFieldOrThrow(final @NotNull Class<?> cls, final boolean declared, final @NotNull String name) {
        try {
            if(declared)
                return cls.getDeclaredField(name);
            else return cls.getField(name);
        } catch(final NoSuchFieldException ex) {
            throw new LookupException(ex);
        }
    }

}
