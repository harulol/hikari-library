package dev.hawu.plugins.api.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents another type of property but its value
 * is not constant and can be changed by the user.
 *
 * @param <T> The type of the property.
 * @since 1.5
 */
public final class MutableProperty<T> extends Property<T> {

    private boolean nullable = false;

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
