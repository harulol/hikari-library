package dev.hawu.plugins.api.gui.brushes;

import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.gui.GuiModel;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * The brush to apply a bordered pattern to a model.
 *
 * @since 2.0
 */
public final class BorderBrush {

    private final List<Supplier<GuiComponent<?>>> components;
    private final GuiModel model;

    /**
     * Creates a new border brush for the provided model.
     *
     * @param model the model
     * @param list  the list of component suppliers
     * @since 2.0
     */
    public BorderBrush(final @NotNull GuiModel model, final @NotNull List<Supplier<GuiComponent<?>>> list) {
        this.model = model;
        this.components = list;
    }

    private List<Integer> getIterable() {
        final List<Integer> list = new ArrayList<>();
        final Inventory inv = this.model.getInventory();
        if(inv.getSize() <= 18) return list;

        // 0 -> 8, then 8 -> last slot, last slot -> first of last row, then back to 0.
        // We need it in that order.
        IntStream.range(0, 8).forEach(list::add);
        IntStream.range(8, inv.getSize() - 1).filter(i -> i % 9 == 8).forEach(list::add);
        for(int i = inv.getSize() - 1; i > inv.getSize() - 9; i--) list.add(i); // No int streams for backwards?
        for(int i = inv.getSize() - 9; i > 0; i -= 9) list.add(i);

        return list;
    }

    /**
     * Apply the border pattern to the model.
     *
     * @since 2.0
     */
    public void apply() {
        final List<Integer> iterable = getIterable();
        for(int i = 0; i < iterable.size(); i++)
            model.mount(iterable.get(i), components.get(i % components.size()).get());
    }

    /**
     * The builder for the border brush.
     *
     * @since 2.0
     */
    public static class Builder {

        private final List<Supplier<GuiComponent<?>>> components = new ArrayList<>();
        private GuiModel model;

        /**
         * Creates a new builder for the border brush.
         *
         * @since 2.0
         */
        public Builder() {
            // Intentionally left blank.
        }

        /**
         * Sets the model for the border brush.
         *
         * @param model the model
         * @return the builder
         * @since 2.0
         */
        @NotNull
        public Builder model(final @NotNull GuiModel model) {
            this.model = model;
            return this;
        }

        /**
         * Adds a GUI component for the border brush.
         *
         * @param supplier the supplier for a component
         * @return the same builder
         * @since 2.0
         */
        @NotNull
        public Builder addComponent(final @NotNull Supplier<GuiComponent<?>> supplier) {
            components.add(supplier);
            return this;
        }

        /**
         * Builds the border brush.
         *
         * @return the brush
         * @since 2.0
         */
        @NotNull
        public BorderBrush build() {
            Objects.requireNonNull(this.model, "Model must be non-null");
            if(!this.model.getInventory().getType().name().endsWith("CHEST"))
                throw new IllegalArgumentException("Model must be of chest type.");
            return new BorderBrush(this.model, this.components);
        }

    }

}
