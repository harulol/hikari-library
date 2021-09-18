package dev.hawu.plugins.api.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static dev.hawu.plugins.api.Strings.color;

/**
 * Represents a modifiable and interactive inventory
 * that may be used as GUIs.
 *
 * @since 1.0
 */
public final class Widget implements InventoryHolder, Listener {

    private final Inventory inventory;
    private final Button<?>[] contents;

    private Consumer<InventoryClickEvent> outsideHandler;

    /**
     * Constructs a widget directly as a double chest inventory
     * with a fixed number of slots and a name.
     *
     * @param size  The size of the inventory.
     * @param title The title of the inventory.
     * @since 1.0
     */
    public Widget(@NotNull final InventorySize size, @Nullable final String title) {
        inventory = Bukkit.createInventory(this, size.getSlots(), title != null ? color(title) : null);
        contents = new Button<?>[inventory.getSize()];
    }

    /**
     * Constructs a widget using an inventory type as base, such
     * as anvils, crafting tables, etc. with a name.
     *
     * @param type  The type of the inventory.
     * @param title The title of the inventory.
     * @since 1.0
     */
    public Widget(@NotNull final InventoryType type, @NotNull final String title) {
        inventory = Bukkit.createInventory(this, type, title);
        contents = new Button<?>[inventory.getSize()];
    }

    /**
     * Creates a button that holds the value of type {@link T} at a fixed slot,
     * and applies the function provided to transform the displayed item stack.
     *
     * @param slot The slot of the button.
     * @param item The initial item of the button.
     * @param <T>  The type of the value held in the button.
     * @return The button after registering with an item stack.
     * @since 1.0
     */
    @NotNull
    public <T> Button<T> createButton(final int slot, @NotNull final ItemStack item) {
        final Button<T> button = new Button<>(this, slot, item);
        contents[slot] = button;
        return button;
    }

    /**
     * Attempts to retrieve a wildcard type {@link Button} at the
     * provided index.
     *
     * @param slot The slot to retrieve at.
     * @return The button there, or {@code null}.
     * @throws IndexOutOfBoundsException If the slot index is larger than or equal to the array size.
     * @since 1.0
     */
    @Nullable
    public Button<?> getButton(final int slot) {
        return contents[slot];
    }

    /**
     * Attempts to retrieve a typed generic of {@link Button} at the
     * provided index.
     *
     * @param slot The slot to retrieve at.
     * @param <T>  The type of the value.
     * @return A {@link Button} there, or {@code null}.
     * @throws IndexOutOfBoundsException If the slot index is larger than or equal to the array size.
     * @since 1.0
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> Button<T> getButtonAs(final int slot) {
        return (Button<T>) getButton(slot);
    }

    /**
     * Sets the outside handler for the widget that handles
     * events and clicks that were not fired on a button in the widget.
     *
     * @param outsideHandler The consumer to be invoked.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public Widget setOutsideHandler(final Consumer<InventoryClickEvent> outsideHandler) {
        this.outsideHandler = outsideHandler;
        return this;
    }

    /**
     * Updates the item at the specified index. This overrides the
     * live inventory's slot with the ones specified in the array.
     *
     * @param slot The slot to update at.
     * @return The same receiver.
     * @throws IndexOutOfBoundsException If the slot is out of bounds.
     * @since 1.0
     */
    public Widget update(final int slot) {
        if(contents[slot] == null) inventory.setItem(slot, null);
        else inventory.setItem(slot, contents[slot].getDisplay());
        return this;
    }

    /**
     * Updates and makes all changes to the buttons live iteratively.
     *
     * @return The same receiver.
     * @since 1.0
     */
    public Widget update() {
        for(int i = 0; i < contents.length; i++) {
            update(i);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Opens the underlying inventory to a human entity.
     *
     * @param entity The entity to open to.
     * @since 1.0
     */
    public void open(@NotNull final HumanEntity entity) {
        entity.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onClick(final InventoryClickEvent event) {
        if(event.getInventory() != inventory && event.getInventory().getHolder() != this) return;
        if(event.getRawSlot() < inventory.getSize()) {
            if(contents[event.getRawSlot()] != null) contents[event.getRawSlot()].handle(event);
        } else if(event.getSlotType() == InventoryType.SlotType.OUTSIDE || event.getRawSlot() > inventory.getSize() && outsideHandler != null)
            outsideHandler.accept(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onClose(final InventoryCloseEvent event) {
        if(event.getInventory() == inventory || event.getInventory().getHolder() == this)
            HandlerList.unregisterAll(this);
    }

}
