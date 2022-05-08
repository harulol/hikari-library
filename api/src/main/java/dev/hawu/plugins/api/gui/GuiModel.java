package dev.hawu.plugins.api.gui;

import dev.hawu.plugins.api.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Represents a virtual inventory that can act as
 * an interactive GUI.
 *
 * @since 1.2
 */
public final class GuiModel implements InventoryHolder {

    private final Inventory inventory;
    private final GuiElement<?>[] elements;

    private Consumer<InventoryClickEvent> outsideClickHandler = event -> event.setCancelled(true);
    private Consumer<InventoryDragEvent> dragHandler = event -> event.setCancelled(true);
    private Runnable onCloseHook;
    private boolean cooldown = true;
    private boolean unmountOnClose = false;

    /**
     * Constructs a model using a chest inventory with given size.
     * <p>
     * Size must be a multiple of 9, ranging from 9 (1 row)
     * to 54 (6 rows).
     *
     * @param size The size of the inventory.
     * @since 1.2
     */
    public GuiModel(final int size) {
        this.inventory = Bukkit.createInventory(this, size);
        this.elements = new GuiElement<?>[inventory.getSize()];
    }

    /**
     * Constructs a model using a chest inventory with given size
     * and custom colorized title.
     *
     * @param size  The size of the inventory.
     * @param title The title of the inventory.
     * @since 1.2
     */
    public GuiModel(final int size, final @NotNull String title) {
        this.inventory = Bukkit.createInventory(this, size, Strings.color(title));
        this.elements = new GuiElement<?>[inventory.getSize()];
    }

    /**
     * Constructs a model using a different inventory type.
     *
     * @param type The inventory type.
     * @since 1.2
     */
    public GuiModel(final @NotNull InventoryType type) {
        this.inventory = Bukkit.createInventory(this, type);
        this.elements = new GuiElement<?>[inventory.getSize()];
    }

    /**
     * Constructs a model using a different inventory type and
     * a custom colorized title.
     *
     * @param type  The inventory type.
     * @param title The title of the inventory.
     * @since 1.2
     */
    public GuiModel(final @NotNull InventoryType type, final @NotNull String title) {
        this.inventory = Bukkit.createInventory(this, type, Strings.color(title));
        this.elements = new GuiElement<?>[inventory.getSize()];
    }

    /**
     * Configures whether this inventory should automatically unmount
     * everything as the inventory closes.
     *
     * @param v Whether to unmount
     * @since 1.6
     */
    public void setUnmountOnClose(final boolean v) {
        this.unmountOnClose = v;
    }

    /**
     * Mounts an element at the given index (slot). Any elements
     * at this index will be unmounted before mounting
     * this new element.
     *
     * @param index   The index of the slot.
     * @param element The element to mount.
     * @since 1.2
     */
    public void mount(final int index, final @Nullable GuiElement<?> element) {
        unmount(index);
        this.elements[index] = element;
        if(element != null) {
            element.mount(this, index);
        }
    }

    /**
     * Mounts an element at the given coordinates. Any elements
     * at this index will be unmounted before mounting
     * this new element.
     *
     * @param x       The x coordinate.
     * @param y       The y coordinate.
     * @param element The element to mount.
     * @since 1.6
     */
    public void mount(final int x, final int y, final @Nullable GuiElement<?> element) {
        mount(x + y * 9, element);
    }

    /**
     * Checks if the model is currently affected
     * by the cooldown checks in inventory click events.
     *
     * @return Whether it is affected.
     * @since 1.2
     */
    public boolean hasCooldown() {
        return cooldown;
    }

    /**
     * Specifies that this model should not be logged
     * in inventory click events, and all events will
     * pass through this model's handler.
     *
     * @since 1.2
     */
    public void noCooldown() {
        this.cooldown = false;
    }

    /**
     * Configures what to run when this inventory model is closed
     * by the viewers.
     *
     * @param runnable The function to run.
     * @since 1.2
     */
    public void onClose(final @NotNull Runnable runnable) {
        this.onCloseHook = runnable;
    }

    /**
     * Configures the consumer to run when this inventory model is invoked
     * in an InventoryDragEvent.
     *
     * @param handler The handler to run.
     * @since 1.2
     */
    public void onDrag(final @NotNull Consumer<@NotNull InventoryDragEvent> handler) {
        this.dragHandler = handler;
    }

    /**
     * Configures the consumer to be invoked when the viewer
     * clicks outside this inventory.
     *
     * @param handler The consumer to invoke.
     * @since 1.2
     */
    public void onOutsideClick(final @NotNull Consumer<@NotNull InventoryClickEvent> handler) {
        this.outsideClickHandler = handler;
    }

    /**
     * Unmounts the element at the given index.
     *
     * @param index The index of the slot.
     * @since 1.2
     */
    public void unmount(final int index) {
        final GuiElement<?> element = this.elements[index];
        if(element != null) {
            element.unmount();
        }
        this.elements[index] = null;
    }

    /**
     * Unmounts the element at the given coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @since 1.6
     */
    public void unmount(final int x, final int y) {
        unmount(x + y * 9);
    }

    /**
     * Updates and re-renders the item at the given index.
     *
     * @param index The index to re-render at.
     * @since 1.2
     */
    public void update(final int index) {
        final GuiElement<?> element = elements[index];
        try {
            if(element != null) {
                inventory.setItem(index, element.render());
            } else {
                inventory.setItem(index, new ItemStack(Material.AIR));
            }
        } catch(final Exception e) {
            if(element != null) element.elementDidCatch(e);
            else throw e;
        }
    }

    /**
     * Updates every slot.
     *
     * @since 1.2
     */
    public void update() {
        IntStream.range(0, elements.length).forEach(this::update);
    }

    /**
     * Get the overall size of the inventory.
     *
     * @return The size of this inventory.
     * @since 1.2
     */
    public int getSize() {
        return this.inventory.getSize();
    }

    /**
     * Retrieves the element at the index.
     *
     * @param index The index to retrieve at.
     * @return The element there, nullable.
     * @since 1.2
     */
    @Nullable
    public GuiElement<?> getElement(final int index) {
        return elements[index];
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     * @since 1.2
     */
    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Opens this inventory to a player.
     *
     * @param entity The entity to open to.
     * @since 1.2
     */
    public void open(final @NotNull HumanEntity entity) {
        update();
        entity.openInventory(this.getInventory());
    }

    void handleClose() {
        if(this.onCloseHook != null) this.onCloseHook.run();
        if(unmountOnClose) IntStream.range(0, elements.length).forEach(this::unmount);
    }

    void handleClick(final @NotNull InventoryClickEvent event) {
        if(event.getSlotType() == SlotType.OUTSIDE || event.getRawSlot() >= getSize()) {
            if(outsideClickHandler != null) outsideClickHandler.accept(event);
            return;
        }

        final GuiElement<?> element = elements[event.getRawSlot()];
        if(element != null) element.handleClick(event);
    }

    void handleDrag(final @NotNull InventoryDragEvent event) {
        if(dragHandler != null) dragHandler.accept(event);
    }

}
