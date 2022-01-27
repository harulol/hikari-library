package dev.hawu.plugins.api.reflect;

import dev.hawu.plugins.api.collections.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for accessing class members reflectively
 * without knowing much about said members.
 *
 * @since 1.0
 */
public final class FuzzyReflects {

    private FuzzyReflects() {}

    /**
     * Attempts to search, match and return the first method with the method type
     * from the provided class and superclasses.
     *
     * @param cls  The class to start looking up.
     * @param type The type of the method to match.
     * @return Always a non-null method wrapper.
     * @since 1.0
     */
    @NotNull
    public static Property<Method> searchMethod(@NotNull final Class<?> cls, @NotNull final MethodType type) {
        for(final Method method : cls.getDeclaredMethods()) {
            if(Arrays.equals(method.getParameterTypes(), type.parameterArray())) return Property.of(method);
        }
        if(cls.getSuperclass() != null) return searchMethod(cls.getSuperclass(), type);
        return Property.empty();
    }

    /**
     * Attempts to search, match and return the first field with the type provided
     * from the provided class and its superclasses.
     *
     * @param cls  The class to start looking up.
     * @param type The type of the field to retrieve.
     * @return Always a non-null field wrapper.
     * @since 1.0
     */
    @NotNull
    public static Property<Field> searchField(@NotNull final Class<?> cls, @NotNull final Class<?> type) {
        for(final Field field : cls.getDeclaredFields()) {
            if(field.getDeclaringClass() == type) return Property.of(field);
        }
        if(cls.getSuperclass() != null) return searchField(cls, type);
        return Property.empty();
    }

    /**
     * Collects all declared fields from the provided class and all of
     * its superclasses.
     *
     * @param cls  The class to start looking up.
     * @param type The type of the field to look for.
     * @return A non-null list of fields of type provided.
     * @since 1.0
     */
    @NotNull
    public static List<Field> collectFields(@NotNull final Class<?> cls, @Nullable final Class<?> type) {
        final List<Field> fields = new ArrayList<>();
        for(final Field field : cls.getDeclaredFields()) {
            if(type != null && field.getDeclaringClass() != type) continue;
            fields.add(field);
        }
        if(cls.getSuperclass() != null) fields.addAll(collectFields(cls.getSuperclass(), type));
        return fields;
    }

}
