package dev.hawu.plugins.api.beta.reflect;

import dev.hawu.plugins.api.reflect.MetafactoryBuilder;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.*;

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

    private static String getInvokedName(final @NotNull Class<?> clazz) {
        if(!clazz.isInterface())
            throw new IllegalArgumentException("Invalid functional interface passed in");
        switch(clazz.getName()) {
            case "java.util.function.Runnable":
                return "run";
            case "java.util.function.Supplier":
                return "get";
            case "java.util.function.Consumer":
            case "java.util.function.BiConsumer":
                return "accept";
            case "java.util.function.Function":
            case "java.util.function.BiFunction":
                return "apply";
            default:
                throw new IllegalArgumentException("Invalid functional interface passed in");
        }
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
