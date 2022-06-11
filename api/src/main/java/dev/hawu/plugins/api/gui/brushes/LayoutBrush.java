package dev.hawu.plugins.api.gui.brushes;

import dev.hawu.plugins.api.gui.GuiElement;
import dev.hawu.plugins.api.gui.GuiModel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A brush that can be used to quickly map elements
 * in a layout to a given GUI Model.
 *
 * @since 1.7
 */
public final class LayoutBrush {

    private final GuiModel model;
    private final Map<String, Set<Integer>> layout;

    /**
     * Makes a new layout brush.
     *
     * @param model  the model to apply to
     * @param layout the layout mapping
     * @since 1.7
     */
    public LayoutBrush(final @NotNull GuiModel model, final @NotNull String... layout) {
        this.model = model;
        this.layout = new HashMap<>();
        makeLayout(layout);
    }

    private void addTo(final String key, final int value) {
        if (!layout.containsKey(key)) {
            layout.put(key, new HashSet<>());
        }
        layout.get(key).add(value);
    }

    private void makeLayout(final @NotNull String... layout) {
        int index = 0;
        for (final String line : layout) {
            for (final char key : line.toCharArray()) {
                addTo(String.valueOf(key), index);
                index++;
            }
        }
    }

    /**
     * Applies to all slots bound to the given key
     * with the element provided by the supplier.
     * <p>
     * If the key does not exist, this does nothing.
     * <p>
     * Element can not be cloned, so this function accepts
     * a supplier, sadly.
     *
     * @param key     The key to apply to
     * @param element The element to apply
     * @since 1.7
     */
    public void apply(final char key, final @NotNull Supplier<GuiElement<?>> element) {
        if (!layout.containsKey(String.valueOf(key))) {
            return;
        }
        layout.get(String.valueOf(key)).forEach(index -> model.mount(index, element.get()));
    }

    /**
     * The builder class for a layout brush.
     *
     * @since 1.7
     */
    public static class Builder {

        private GuiModel model;
        private String[] layout;

        Builder() {
        }

        /**
         * Sets the model for the layout brush.
         *
         * @param model the model
         * @return the same builder
         */
        @NotNull
        public Builder setModel(final @NotNull GuiModel model) {
            this.model = model;
            return this;
        }

        /**
         * Sets the layout for the model.
         *
         * @param layout the layout
         * @return the same builder
         * @since 1.7
         */
        @NotNull
        public Builder setLayout(final @NotNull String... layout) {
            this.layout = layout;
            return this;
        }

        /**
         * Builds the layout brush.
         *
         * @return the layout brush
         * @since 1.7
         */
        @NotNull
        public LayoutBrush build() {
            return new LayoutBrush(model, layout);
        }

    }

}
