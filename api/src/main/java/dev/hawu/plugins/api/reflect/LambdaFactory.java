package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Represents a factory for creating via {@link LambdaMetafactory}
 * for small methods, those with fewer than two parameters.
 * <p>
 * For bigger methods, please use {@link LambdaMetafactory} directly or via
 * a builder structure with {@link MetafactoryBuilder}.
 *
 * @since 2.0
 */
public final class LambdaFactory {

    /**
     * This returns an array of method types.
     * <p>
     * First being the type including the functional interface, and the type parameters.
     * Second being the type after erasure.
     * Third being the real method with reified signatures.
     *
     * @param method the method to seed
     * @return the array of method types.
     */
    private static MethodType[] generateFactoryMethodType(final @NotNull Class<?> instanceType, final @NotNull Method method) {
        final MethodType[] types = new MethodType[3];
        final boolean returnsVoid = method.getReturnType() == void.class;
        final boolean isStatic = Modifier.isStatic(method.getModifiers());

        // Generate the invokedType, which should be one of the functional interfaces.
        switch(method.getParameterCount()) {
            case 0:
                types[0] = isStatic ? MethodType.methodType(returnsVoid ? Runnable.class : Supplier.class)
                    : MethodType.methodType(returnsVoid ? Runnable.class : Supplier.class, instanceType);
                types[1] = MethodType.methodType(returnsVoid ? void.class : Object.class);
                types[2] = MethodType.methodType(returnsVoid ? void.class : method.getReturnType());
                break;
            case 1:
                types[0] = isStatic ? MethodType.methodType(returnsVoid ? Consumer.class : Function.class)
                    : MethodType.methodType(returnsVoid ? Consumer.class : Function.class, instanceType);
                types[1] = MethodType.methodType(returnsVoid ? void.class : Object.class, Object.class);
                types[2] = MethodType.methodType(returnsVoid ? void.class : method.getReturnType(), method.getParameterTypes()[0]);
                break;
            case 2:
                types[0] = isStatic ? MethodType.methodType(returnsVoid ? BiConsumer.class : BiFunction.class)
                    : MethodType.methodType(returnsVoid ? BiConsumer.class : BiFunction.class, instanceType);
                types[1] = MethodType.methodType(returnsVoid ? void.class : Object.class, Object.class, Object.class);
                types[2] = MethodType.methodType(returnsVoid ? void.class : method.getReturnType(), method.getParameterTypes()[0], method.getParameterTypes()[1]);
                break;
            default:
                throw new IllegalArgumentException("Cannot generate a built-in lambda factory for a method with more than 2 parameters.");
        }

        return types;
    }

    private static boolean belongsToObject(final Method method) {
        try {
            Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch(final Exception exception) {
            return false;
        }
    }

    /**
     * Attempts to find the function name of the functional interface.
     * <p>
     * A functional interface, by definition:
     * <ul>
     *     <li>Must have exactly one abstract method</li>
     *     <li>Synthetic, native or bridge methods do not count</li>
     *     <li>Methods belonging to {@code java.lang.Object} do not count</li>
     *     <li>Static methods do not count</li>
     * </ul>
     *
     * @param clazz The functional interface to find the name of.
     * @return The name of the method.
     * @since 2.0
     */
    public static String getInvokedName(final @NotNull Class<?> clazz) {
        if(!clazz.isInterface())
            throw new IllegalArgumentException("The class " + clazz.getName() + " is not an interface.");

        final List<Method> abstractMethods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> !m.isDefault() && !m.isBridge() && !m.isSynthetic())
            .filter(m -> !Modifier.isStatic(m.getModifiers()) && !Modifier.isNative(m.getModifiers()))
            .filter(m -> !belongsToObject(m))
            .collect(Collectors.toList());

        if(abstractMethods.size() != 1)
            throw new IllegalArgumentException("The class " + clazz.getName() + " must have exactly one abstract method.");

        return abstractMethods.get(0).getName();
    }

    /**
     * Generates a generic call site for the passed in method if valid,
     * with the lookup initialized in {@link LambdaFactory}.
     * <p>
     * This has high chance to fail if:
     * <ul>
     *     <li>The method is private</li>
     *     <li>The method is in another package far away from {@link LambdaFactory}</li>
     * </ul>
     * <p>
     * Provide your own version of {@code MethodHandles.lookup()} to {@link LambdaFactory#generate(Lookup, Method)}
     * for better matching and lower fail rate.
     *
     * @param method the method to generate the call site for.
     * @return the call site.
     * @since 2.0
     */
    public static CallSite generate(final @NotNull Method method) {
        return generate(MethodHandles.lookup(), method);
    }

    /**
     * Generates a generic call site for the passed in method if valid.
     *
     * @param lookup The lookup instance that must be able to see the method.
     * @param method the method to generate the call site for.
     * @return the call site.
     * @since 2.0
     */
    public static CallSite generate(final @NotNull MethodHandles.Lookup lookup, final @NotNull Method method) {
        method.setAccessible(true);

        try {
            final MethodHandle handle = lookup.unreflect(method);
            final MethodType[] types = generateFactoryMethodType(method.getDeclaringClass(), method);
            final String invokedName = getInvokedName(types[0].returnType());

            return LambdaMetafactory.metafactory(
                lookup,
                invokedName,
                types[0],
                types[1],
                handle,
                types[2]
            );
        } catch(final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
