package dev.hawu.plugins.api.gui;

import dev.hawu.plugins.api.collections.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a holder for a state in a GUI element.
 *
 * @param <T> the type of the state
 * @since 1.6
 */
public final class ElementState<T> {

    private final GuiElement<?> hook;
    private T value;

    ElementState(final GuiElement<?> hook, final T initial) {
        this.hook = hook;
        this.value = initial;
    }

    /**
     * Retrieves the state value.
     *
     * @return the state value
     * @since 1.6
     */
    @Nullable
    public T get() {
        return value;
    }

    /**
     * Retrieves the state value non-null.
     *
     * @return the state value non-null
     * @since 1.6
     */
    @NotNull
    public T getNonNull() {
        return Objects.requireNonNull(value);
    }

    /**
     * Retrieves the state in a property wrapper.
     *
     * @return the state in a property wrapper
     * @since 1.6
     */
    @NotNull
    public Property<T> getProperty() {
        return Property.of(value);
    }

    /**
     * Sets the state value.
     *
     * @param value the state value
     * @since 1.6
     */
    public void set(final @Nullable T value) {
        this.value = value;
        this.hook.forceUpdate();
    }

}
