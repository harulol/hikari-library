package dev.hawu.plugins.api.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a wrapper for a read-only value for nullability safety.
 *
 * @param <T> The type of the value.
 * @since 1.5
 */
public class Property<T> {

    private static final Property<?> EMPTY = new Property<>(null);
    protected T value;

    /**
     * Creates a new property with the given value.
     *
     * @param value the value to be contained within the property.
     * @param <T>   the type of the value.
     * @return a new property with the given value.
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Property<T> of(@Nullable T value) {
        if(value == null) return (Property<T>) EMPTY;
        else return new Property<>(value);
    }

    /**
     * Retrieves an instance of an empty property.
     *
     * @param <T> The type of the property.
     * @return The empty property.
     * @since 1.5
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Property<T> empty() {
        return (Property<T>) EMPTY;
    }

    /**
     * Attempts to convert a {@link Optional} to a {@link Property}.
     *
     * @param optional The optional to convert.
     * @param <T>      The type of the property.
     * @return The converted property.
     * @since 1.5
     */
    @NotNull
    public static <T> Property<T> fromOptional(final @NotNull Optional<T> optional) {
        return optional.map(Property::of).orElse(empty());
    }

    Property(T value) {
        this.value = value;
    }

    /**
     * Checks if this property is empty.
     *
     * @return Whether this property is empty.
     * @since 1.5
     */
    public final boolean isPresent() {
        return value != null;
    }

    /**
     * Attempts to retrieve the value in this property,
     * while throwing a {@link NullPointerException} if the value is
     * not present.
     *
     * @return The value in this property.
     * @since 1.5
     */
    @NotNull
    public final T get() {
        return Objects.requireNonNull(value, "The value is not present within the property.");
    }

    /**
     * Reassigns the value within this property.
     * <p>
     * This will fail with an {@link UnsupportedOperationException}
     * if this property instance is immutable.
     *
     * @param newValue The new value to be contained within this property.
     * @since 1.5
     */
    public void set(final @Nullable T newValue) {
        throw new UnsupportedOperationException("Cannot set the value of a property.");
    }

    /**
     * Reassigns the value within this property and returns
     * the property instance, usually for chaining.
     *
     * @param newValue The new value to be contained within this property.
     * @return The property instance.
     * @since 1.5
     */
    public Property<T> value(final @Nullable T newValue) {
        throw new UnsupportedOperationException("Cannot set the value of a property.");
    }

    /**
     * Attempts to retrieve the value within this property,
     * accepting that {@code null} is a valid response.
     *
     * @return The value within this property, or {@code null} if it is not present.
     * @since 1.5
     */
    @Nullable
    public final T orNull() {
        return value;
    }

    /**
     * Attempts to retrieve the value from this property,
     * and returns the other value if this property is empty.
     *
     * @param other The value to be returned if this property is empty.
     * @return The value from this property, or the other value if this property is empty.
     * @since 1.5
     */
    @Nullable
    public final T orElse(final @Nullable T other) {
        return value != null ? value : other;
    }

    /**
     * Attempts to retrieve the value from this property,
     * or retrieves the value from the other property if this property is empty
     * with {@code null} as a valid response.
     *
     * @param other The property to be used if this property is empty.
     * @return The value from this property, or the other property's value if this property is empty.
     * @since 1.5
     */
    @Nullable
    public final T or(final @NotNull Property<T> other) {
        return value != null ? value : other.orNull();
    }

    /**
     * Attempts to retrieve the value, or throws a customized
     * throwable if it is not present.
     *
     * @param throwable The throwable to throw.
     * @param <U>       The type of the throwable.
     * @return The value present.
     * @throws U I think...?
     * @since 1.5
     */
    @NotNull
    public final <U extends Throwable> T orElseThrow(final @NotNull U throwable) throws U {
        if(value == null) throw throwable;
        else return value;
    }

    /**
     * Attempts to retrieve the value, or throws a customized
     * throwable supplied by a function if it is not present.
     *
     * @param supplier The supplier that provides the throwable.
     * @param <U>      The type of the throwable.
     * @return The value if present.
     * @throws U If the value is not present.
     * @since 1.5
     */
    @NotNull
    public final <U extends Throwable> T orElseThrow(final @NotNull Supplier<U> supplier) throws U {
        return orElseThrow(supplier.get());
    }

    /**
     * Applies the mapper function to the value in this property
     * if it is present, otherwise does nothing.
     *
     * @param mapper The mapper function to be applied.
     * @param <R>    The type of the result.
     * @return The result of the mapper function in a property.
     * @since 1.5
     */
    public final <R> Property<R> map(final @NotNull Function<T, R> mapper) {
        return value != null ? of(mapper.apply(value)) : empty();
    }

    /**
     * Performs an action on the value in this property
     * if it is present, otherwise does nothing.
     *
     * @param consumer The consumer to be applied.
     * @return This property.
     * @since 1.5
     */
    public final Property<T> ifPresent(final @NotNull Consumer<T> consumer) {
        if(value != null) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * Converts this instance of immutable property to a mutable property.
     *
     * @return A mutable property.
     * @since 1.5
     */
    @NotNull
    public final Property<T> toMutable() {
        return this instanceof MutableProperty ? this : new MutableProperty<>(value);
    }

    /**
     * Converts this instance of mutable property to an immutable property,
     * if possible.
     *
     * @return An immutable property.
     * @since 1.5
     */
    @NotNull
    public Property<T> toImmutable() {
        return !(this instanceof MutableProperty) ? this : new Property<>(value);
    }

    /**
     * Converts this property to a Java {@link Optional}.
     *
     * @return The Java {@link Optional} representation of this property.
     * @since 1.5
     */
    @NotNull
    public final Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

}
