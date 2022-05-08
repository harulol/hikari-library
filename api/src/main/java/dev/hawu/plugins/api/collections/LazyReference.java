package dev.hawu.plugins.api.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a property that is lazily computed on request.
 *
 * @param <T> the type of the property
 * @since 1.6
 */
public final class LazyReference<T> {

    private final Supplier<T> supplier;
    private T value;

    private LazyReference(final @NotNull T computedValue) {
        this.value = Objects.requireNonNull(computedValue);
        this.supplier = null;
    }

    private LazyReference(final @NotNull Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Creates a new lazily referenced precomputed value.
     *
     * @param value the value
     * @param <K>   the type of the value
     * @return the lazy reference
     * @since 1.6
     */
    @NotNull
    public static <K> LazyReference<K> of(final @NotNull K value) {
        return new LazyReference<>(value);
    }

    /**
     * Creates a new lazy reference, whose value will be computed
     * on the first request.
     *
     * @param supplier the supplier
     * @param <K>      the type of the value
     * @return the lazy reference
     * @since 1.6
     */
    public static <K> LazyReference<K> of(final @NotNull Supplier<K> supplier) {
        return new LazyReference<>(supplier);
    }

    /**
     * Checks if the value is computed yet.
     *
     * @return true if the value is computed
     * @since 1.6
     */
    public boolean isInitialized() {
        return value != null;
    }

    /**
     * Returns the value if it is present, computes then returns otherwise.
     *
     * @return the value
     * @since 1.6
     */
    @NotNull
    public T get() {
        if(value == null) {
            return value = Objects.requireNonNull(supplier).get();
        } else return value;
    }

}
