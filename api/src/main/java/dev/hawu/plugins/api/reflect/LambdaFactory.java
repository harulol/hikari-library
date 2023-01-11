package dev.hawu.plugins.api.reflect;

import com.google.common.collect.Sets;
import dev.hawu.plugins.api.collections.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Represents a factory for creating lambdas via {@link LambdaMetafactory} dynamically
 * for any methods, provided that there is a functional interface with matching
 * signatures.
 * <p>
 * Functional interfaces should be provided via {@link LambdaFactory#registerInterface(Class)}. Please be
 * warned that Java treats primitives and other classes differently, so you actually need at least two versions
 * of functional interfaces if any of them contain a primitive type.
 * <p>
 * For example, the method with signature {@code int(int, String)} will only match any functional interface
 * with functional method {@code int(int, Object)} while failing to match {@code Object(int, Object)} or
 * any of the sorts. But {@code byte(int, String)} will obviously not match functional method {@code int(int, Object)}.
 *
 * @since 2.0
 */
public final class LambdaFactory {

    private static final Set<Class<?>> FUNCTIONAL_INTERFACES = Sets.newHashSet(
        BiConsumer.class, BiFunction.class, BiPredicate.class, BooleanSupplier.class, Consumer.class,
        DoubleBinaryOperator.class, DoubleConsumer.class, DoubleFunction.class, DoublePredicate.class,
        DoubleSupplier.class, DoubleToIntFunction.class, DoubleToLongFunction.class, DoubleUnaryOperator.class,
        Function.class, IntBinaryOperator.class, IntConsumer.class, IntFunction.class, IntPredicate.class,
        IntSupplier.class, IntToDoubleFunction.class, IntToLongFunction.class, IntUnaryOperator.class,
        LongBinaryOperator.class, LongConsumer.class, LongFunction.class, LongPredicate.class,
        LongSupplier.class, LongToDoubleFunction.class, LongToIntFunction.class, LongUnaryOperator.class,
        ObjDoubleConsumer.class, ObjIntConsumer.class, ObjLongConsumer.class, Predicate.class,
        Supplier.class, ToDoubleBiFunction.class, ToDoubleFunction.class, ToIntBiFunction.class,
        ToIntFunction.class, ToLongBiFunction.class, ToLongFunction.class
    );
    private static final Set<Class<?>> PRIMITIVE_CLASSES = Sets.newHashSet(
        byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class, void.class, char.class
    );

    private LambdaFactory() {
    }

    @NotNull
    private static Property<Class<?>> findFunctionalClass(final @NotNull Method method) {
        outer:
        for(final Class<?> cls : FUNCTIONAL_INTERFACES) {
            final Method funcMethod = validateFunctionalInterface(cls);

            if(funcMethod.getParameterCount() != method.getParameterCount()) continue;
            if(getPrimitiveClass(funcMethod.getReturnType()) != getPrimitiveClass(method.getReturnType())) continue;

            final Class<?>[] funcParams = funcMethod.getParameterTypes();
            final Class<?>[] params = method.getParameterTypes();

            for(int i = 0; i < funcParams.length; i++)
                if(getPrimitiveClass(funcParams[i]) != getPrimitiveClass(params[i]))
                    continue outer;

            return Property.of(cls);
        }

        return Property.empty();
    }

    private static Class<?> getPrimitiveClass(final @NotNull Class<?> clazz) {
        if(PRIMITIVE_CLASSES.contains(clazz))
            return clazz;
        return Object.class;
    }

    private static String getSignature(final @NotNull Method method) {
        final StringJoiner joiner = new StringJoiner(",", "(", ")");
        for(final Class<?> param : method.getParameterTypes()) {
            joiner.add(param.getSimpleName());
        }
        return joiner + method.getReturnType().getSimpleName();
    }

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
        final Class<?> returnType = returnsVoid ? void.class : method.getReturnType();
        final boolean isStatic = Modifier.isStatic(method.getModifiers());
        final Class<?>[] objectParameters = new Class<?>[method.getParameterCount()];

        // Fill with primitive types if it has to be, since int is not an Object for example.
        for(int i = 0; i < objectParameters.length; i++)
            objectParameters[i] = getPrimitiveClass(method.getParameterTypes()[i]);

        final Class<?> funcClass = findFunctionalClass(method)
            .orElseThrow(new IllegalArgumentException("Method has unmatchable signature. Please provide interface for " + getSignature(method)));

        types[0] = isStatic ? MethodType.methodType(funcClass) : MethodType.methodType(funcClass, instanceType);
        types[1] = MethodType.methodType(getPrimitiveClass(method.getReturnType()), objectParameters);
        types[2] = MethodType.methodType(returnType, method.getParameterTypes());

        return types;
    }

    private static boolean notBelongsToObject(final Method method) {
        try {
            Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return false;
        } catch(final Exception exception) {
            return true;
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
        return validateFunctionalInterface(clazz).getName();
    }

    /**
     * Attempts to register a functional interface for use in lambda factories
     * for methods with more than two parameters.
     * <p>
     * A functional interface is described as in {@link LambdaFactory#getInvokedName(Class)}.
     *
     * @param clazz the class to register
     * @since 2.0
     */
    public static void registerInterface(final @NotNull Class<?> clazz) {
        validateFunctionalInterface(clazz);
        FUNCTIONAL_INTERFACES.add(clazz);
    }

    private static Method validateFunctionalInterface(final Class<?> clazz) {
        if(!clazz.isInterface())
            throw new IllegalArgumentException("The class " + clazz.getName() + " is not an interface.");

        final List<Method> abstractMethods = getAbstractMethods(clazz);
        if(abstractMethods.size() != 1)
            throw new IllegalArgumentException("The class " + clazz.getName() + " must have exactly one abstract method.");
        return abstractMethods.get(0);
    }

    private static List<Method> getAbstractMethods(final Class<?> cls) {
        return Arrays.stream(cls.getDeclaredMethods())
            .filter(m -> !m.isDefault() && !m.isBridge() && !m.isSynthetic())
            .filter(m -> !Modifier.isStatic(m.getModifiers()) && !Modifier.isNative(m.getModifiers()))
            .filter(LambdaFactory::notBelongsToObject)
            .collect(Collectors.toList());
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

    /**
     * Attempts to invoke a callsite with the instance.
     *
     * @param instance   the instance to invoke the callsite with.
     * @param callSite   the callsite to invoke.
     * @param parameters the parameters to invoke the callsite with.
     * @return the result of the callsite.
     * @since 2.0
     */
    @Nullable
    public static Object invoke(final @NotNull Object instance, final @NotNull CallSite callSite,
                                final @Nullable Object... parameters) {
        try {
            final Object functional = callSite.getTarget().bindTo(instance).invoke();
            final Method method = getAbstractMethods(functional.getClass()).get(0);
            return method.invoke(functional, parameters);
        } catch(final Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * Attempts to invoke a callsite without an instance for static methods.
     *
     * @param callSite   the callsite to invoke.
     * @param parameters the parameters to invoke the callsite with.
     * @return the result of the callsite.
     * @since 2.0
     */
    @Nullable
    public static Object invokeStatic(final @NotNull CallSite callSite, final @Nullable Object... parameters) {
        try {
            final Object functional = callSite.getTarget().invoke();
            final Method method = getAbstractMethods(functional.getClass()).get(0);
            return method.invoke(functional, parameters);
        } catch(final Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

}
