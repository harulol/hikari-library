package dev.hawu.plugins.api.inventories.style;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a style with a list of item stacks
 * that can be cycled through.
 *
 * @since 1.0
 */
public final class CyclableStyle implements Style {

    private final List<ItemStack> list;
    private int pointer;

    /**
     * Constructs a cyclable style with an empty list.
     *
     * @since 1.0
     */
    public CyclableStyle() {
        this.list = new ArrayList<>();
    }

    /**
     * Constructs a cyclable style with an initial list of items.
     * <p>
     * Changes to the underlying list in this style via {@link CyclableStyle#add(ItemStack)}
     * and similar methods will not mutate the passed in list.
     *
     * @param items The items to initialize with.
     * @since 1.0
     */
    public CyclableStyle(@NotNull final List<@NotNull ItemStack> items) {
        this.list = new ArrayList<>(items); // Wrap in case the list passed in is unmodifiable.
    }

    /**
     * Constructs a cyclable style with an initial list of items,
     * passed in via a varargs parameter.
     *
     * @param items The items to initialize with.
     * @since 1.0
     */
    public CyclableStyle(@NotNull final ItemStack @NotNull ... items) {
        this.list = new ArrayList<>(Arrays.asList(items));
    }

    /**
     * Adds an item to the list.
     *
     * @param item The item to add.
     * @since 1.0
     */
    public void add(@NotNull final ItemStack item) {
        list.add(item);
    }

    /**
     * Adds an item to the list at a specified index.
     *
     * @param index The index to add at.
     * @param item  The item to add.
     * @since 1.0
     */
    public void add(final int index, @NotNull final ItemStack item) {
        list.add(index, item);
    }

    /**
     * Gets the current pointer of the cycle.
     *
     * @return The current pointer.
     * @since 1.0
     */
    public int getPointer() {
        return this.pointer;
    }

    /**
     * Sets the current position of the pointer in the cycle.
     * <p>
     * This operation overrides the field directly, so {@link CyclableStyle#getDisplay()} calls
     * will throw an {@link AssertionError} if the set index is out of bounds.
     *
     * @param value The new value of the position.
     * @since 1.0
     */
    @Deprecated
    public void setPointer(final int value) {
        this.pointer = value;
    }

    /**
     * Cycles the pointer forwards by a set amount. If the pointer
     * overflows, it will be repositioned at {@code 0}, then continue
     * cycling.
     *
     * @param amount The amount of times to cycle.
     * @since 1.0
     */
    public void cycleForwards(final int amount) {
        final int leftover = amount % list.size();
        pointer = pointer + leftover > list.size() ? pointer + leftover - list.size() : pointer + leftover;
    }

    /**
     * Cycles the pointer backwards by a set amount. If the pointer
     * underflows, it will be repositioned at {@code size - 1}, then
     * continue cycling.
     *
     * @param amount The amount of times to cycle.
     * @since 1.0
     */
    public void cycleBackwards(final int amount) {
        final int leftover = amount % list.size();
        pointer = pointer - leftover < 0 ? pointer - leftover + list.size() : pointer - leftover;
    }

    /**
     * Shorthand for calling {@link CyclableStyle#getDisplay()} and {@link CyclableStyle#cycleForwards(int)}.
     * <p>
     * Retrieves the item at the position of the pointer, then cycles the pointer forwards
     * by exactly {@code 1}.
     *
     * @return The item at the position of the pointer, before cycling.
     * @since 1.0
     */
    @NotNull
    public ItemStack next() {
        final ItemStack value = list.get(pointer);
        cycleForwards(1);
        return value;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This operation only retrieves the item present at the position of the pointer,
     * and does not advance the pointer.
     * <p>
     * A call to {@link CyclableStyle#cycleForwards(int)} will be needed if the pointer
     * needs to be advanced.
     *
     * @throws AssertionError If the pointer is not in bound.
     * @since 1.0
     */
    @Override
    public @NotNull ItemStack getDisplay() {
        assert pointer < list.size() : "The pointer is somehow larger than or equal to the size of the cycle.";
        return list.get(pointer);
    }

}
