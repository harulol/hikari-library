package dev.hawu.plugins.api.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Represents another type of property but its value
 * is not constant and can be changed by the user.
 *
 * @param <T> The type of the property.
 * @since 1.5
 */
public final class MutableProperty<T> extends Property<T> {

    private boolean nullable = false;

    /**
     * Creates a new mutable property with the given value.
     *
     * @param value the value to be contained within the property.
     * @param <T>   the type of the value.
     * @return a new property with the given value.
     * @since 1.6
     */
    @NotNull
    public static <T> Property<T> of(@Nullable T value) {
        return new MutableProperty<>(value);
    }

    /**
     * Retrieves an instance of an empty mutable property.
     *
     * @param <T> The type of the property.
     * @return The empty property.
     * @since 1.6
     */
    @NotNull
    public static <T> Property<T> empty() {
        return new MutableProperty<>(null);
    }

    /**
     * Attempts to convert a {@link Optional} to a {@link Property}.
     *
     * @param optional The optional to convert.
     * @param <T>      The type of the property.
     * @return The converted property.
     * @since 1.6
     */
    @NotNull
    public static <T> Property<T> fromOptional(final @NotNull Optional<T> optional) {
        return optional.map(MutableProperty::of).orElseGet(MutableProperty::empty);
    }

    MutableProperty(final T value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.5
     */
    @Override
    public void set(final @Nullable T value) {
        if(!nullable && value == null)
            throw new IllegalArgumentException("Value can not be null if property is not nullable.");
        this.value = value;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.5
     */
    @Override
    public Property<T> value(@Nullable final T newValue) {
        set(newValue);
        return this;
    }

    /**
     * Makes this mutable property accept null values
     * in {@link Property#set(Object)} and similar methods.
     *
     * @return This property.
     * @since 1.6
     */
    @Override
    @NotNull
    public MutableProperty<T> nullable() {
        nullable = true;
        return this;
    }

}
