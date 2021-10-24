package dev.hawu.plugins.api.inventories;

import org.jetbrains.annotations.Nullable;

/**
 * Represents an element that may have a value
 * and can be presented in a widget.
 *
 * @param <T> The type of the value.
 * @since 1.0
 */
public interface Valuable<T> {

    /**
     * Gets the value of the element.
     *
     * @return The element's value.
     * @since 1.0
     */
    T getValue();

    /**
     * Sets the value of the element to a new one.
     *
     * @param value The new value of the element.
     * @since 1.0
     */
    void setValue(@Nullable T value);

}
