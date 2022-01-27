package dev.hawu.plugins.api.collections;

import org.jetbrains.annotations.Nullable;

/**
 * Represents another type of property but its value
 * is not constant and can be changed by the user.
 *
 * @param <T> The type of the property.
 * @since 1.5
 */
public final class MutableProperty<T> extends Property<T> {

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

}
