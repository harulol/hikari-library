package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.inventories.style.Style;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a widget's element that can have a style.
 *
 * @param <S> The type of the style.
 * @since 1.0
 */
public interface Styleable<S extends Style> {

    /**
     * Gets the style of the element.
     *
     * @return The style of the element.
     * @since 1.0
     */
    @NotNull
    S getStyle();

}
