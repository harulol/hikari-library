package dev.hawu.plugins.api.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Represents a builder to build an invocation
 * to the metafactory function.
 *
 * @since 1.3
 */
public final class MetafactoryBuilder {

    private final MethodHandles.Lookup caller;
    private String invokedName;
    private MethodType invokedType;
    private MethodType samMethodType;
    private MethodHandle implMethod;
    private MethodType instantiatedMethodType;

    MetafactoryBuilder(final @NotNull MethodHandles.Lookup caller) {
        this.caller = caller;
    }

    /**
     * Sets the name of the method of the functional
     * interface.
     *
     * @param invokedName Said name
     * @return This builder
     * @since 1.3
     */
    @NotNull
    public MetafactoryBuilder invokedName(final @NotNull String invokedName) {
        this.invokedName = invokedName;
        return this;
    }

    /**
     * Sets the type of the functional interface.
     *
     * @param invokedType Said type
     * @return This builder
     * @since 1.3
     */
    @NotNull
    public MetafactoryBuilder invokedType(final @NotNull MethodType invokedType) {
        this.invokedType = invokedType;
        return this;
    }

    /**
     * Sets the method type of the functional interface's method.
     *
     * @param samMethodType Said method type
     * @return This builder
     * @since 1.3
     */
    @NotNull
    public MetafactoryBuilder samMethodType(final @NotNull MethodType samMethodType) {
        this.samMethodType = samMethodType;
        return this;
    }

    /**
     * Sets the method handle to convert.
     *
     * @param implMethod Said method handle
     * @return This builder
     * @since 1.3
     */
    @NotNull
    public MetafactoryBuilder implMethod(final @NotNull MethodHandle implMethod) {
        this.implMethod = implMethod;
        return this;
    }

    /**
     * Sets the method type of the instantiated method.
     *
     * @param instantiatedMethodType Said method type
     * @return This builder
     * @since 1.3
     */
    @NotNull
    public MetafactoryBuilder instantiatedMethodType(final @NotNull MethodType instantiatedMethodType) {
        this.instantiatedMethodType = instantiatedMethodType;
        return this;
    }

    /**
     * Builds the callsite, and returns null if it failed.
     *
     * @return The callsite
     * @since 1.3
     */
    @Nullable
    public CallSite build() {
        return UncheckedHandles.metafactory(caller, invokedName, invokedType,
            samMethodType, implMethod, instantiatedMethodType);
    }

}
