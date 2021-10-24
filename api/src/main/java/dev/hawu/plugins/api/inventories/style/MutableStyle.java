package dev.hawu.plugins.api.inventories.style;

import dev.hawu.plugins.api.inventories.Widget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a style that has a mutable
 * representation.
 *
 * @since 1.1
 */
public final class MutableStyle implements Style {

    private ItemStack item;
    private Widget widget;
    private int slot;

    /**
     * Constructs a new mutable style.
     *
     * @param initial The initial item to display as.
     * @since 1.1
     */
    public MutableStyle(final @NotNull ItemStack initial) {
        this.item = initial;
    }

    /**
     * Sets the new value for the item to display.
     *
     * @param item The new item value.
     * @since 1.1
     */
    public void setItem(final @NotNull ItemStack item) {
        this.item = item;
        widget.update(slot);
    }

    /**
     * Applies the provided transformer to the backing stack.
     *
     * @param consumer The function to apply.
     * @since 1.1
     */
    public void transform(@NotNull final Consumer<ItemStack> consumer) {
        consumer.accept(item);
        widget.update(slot);
    }

    /**
     * The item stack that should be used to display
     * at the moment of invocation.
     *
     * @return The item stack to display.
     * @since 1.0
     */
    @Override
    public @NotNull ItemStack getDisplay() {
        return item;
    }

}
