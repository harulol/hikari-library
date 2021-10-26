package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.inventories.style.StaticStyle;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a typical Minecraft server-side in-game GUIs
 * using known interfaces like double chest inventories, furnaces,
 * crafting tables, hoppers, etc.
 *
 * @since 1.0
 */
public final class Widget implements InventoryHolder {

    private final Inventory inventory;
    private final Clickable[] content;

    private Consumer<InventoryClickEvent> outsideHandler;
    private final List<Integer> slots = new ArrayList<>();
    private BukkitTask task;

    /**
     * Constructs a double-chest widget with a fixed amount
     * of slots and an optional title.
     *
     * @param size  The size of the widget.
     * @param title The title of the widget, colorized.
     * @since 1.0
     */
    public Widget(@NotNull final InventorySize size, @Nullable final String title) {
        this.inventory = Bukkit.createInventory(this, size.getSlots(), Strings.color(title));
        this.content = new Clickable[this.inventory.getSize()];
    }

    /**
     * Constructs a specific widget of an inventory type,
     * with a fixed amount of slots and an optional title.
     *
     * @param type  The type of the inventory.
     * @param title The title of the widget, colorized.
     * @since 1.0
     */
    public Widget(@NotNull final InventoryType type, @Nullable final String title) {
        this.inventory = Bukkit.createInventory(this, type, Strings.color(title));
        this.content = new Clickable[this.inventory.getSize()];
    }

    /**
     * Constructs a widget by copying contents from an inventory.
     *
     * @param inv   The inventory to copy from.
     * @param title The title of the inventory.
     * @since 2.1
     */
    public Widget(@NotNull final Inventory inv, @NotNull final String title) {
        if(inv.getType() == InventoryType.CHEST)
            this.inventory = Bukkit.createInventory(this, inv.getSize(), Strings.color(title));
        else this.inventory = Bukkit.createInventory(this, inv.getType(), Strings.color(title));
        this.content = new Clickable[this.inventory.getSize()];

        for(int i = 0; i < inv.getSize(); i++) {
            this.content[i] = new Button<>(new StaticStyle(inv.getItem(i)));
        }
    }

    /**
     * Retrieves the item at a known index.
     *
     * @param index The index to retrieve at.
     * @return A {@link Clickable} instance if available, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public Clickable get(final int index) {
        return content[index];
    }

    /**
     * Retrieves the item at a known index, cast to a {@link Styleable}
     * if possible.
     *
     * @param index The index to retrieve at.
     * @return A {@link Styleable} instance if available, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public Styleable<?> getStyleable(final int index) {
        return (Styleable<?>) content[index];
    }

    /**
     * Retrieves the item at a known index, cast to a {@link Valuable}
     * if possible.
     *
     * @param index The index to retrieve at.
     * @return A {@link Valuable} instance if available, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public Valuable<?> getValuable(final int index) {
        return (Valuable<?>) content[index];
    }

    /**
     * Retrieves the item at a known index, cast to a {@link Button}
     * if possible.
     *
     * @param index The index to retrieve at.
     * @return A {@link Button} instance if available, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public Button<?> getButton(final int index) {
        return (Button<?>) content[index];
    }

    /**
     * Retrieves the item at a known index, cast to a {@link ValuedButton}.
     *
     * @param index The index to retrieve at.
     * @return A {@link ValuedButton} instance if available, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public ValuedButton<?, ?> getValuedButton(final int index) {
        return (ValuedButton<?, ?>) content[index];
    }

    /**
     * Puts an instance of {@link Clickable} at position index.
     *
     * @param index     The index to put at.
     * @param clickable The clickable to put.
     * @since 1.0
     */
    public void put(final int index, @Nullable final Clickable clickable) {
        content[index] = clickable;
    }

    /**
     * Attempts to update all slots present within this inventory.
     *
     * @since 1.0
     */
    public void update() {
        for(int i = 0; i < content.length; i++)
            update(i);
    }

    /**
     * Cancels the currently updating task.
     *
     * @since 1.1
     */
    public void cancelTask() {
        if(this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    /**
     * Sets up a timer for this widget to keep updating
     * until no one is viewing the inventory anymore.
     *
     * @param plugin The plugin responsible for the task.
     * @param millis The number of milliseconds between each run.
     * @since 1.1
     */
    public void setInterval(final @NotNull JavaPlugin plugin, final long millis) {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if(inventory.getViewers().isEmpty()) {
                    cancelTask();
                    return;
                }
                slots.forEach(Widget.this::update);
            }
        }.runTaskTimer(plugin, 0, millis / 50);
    }

    /**
     * Adds slots for {@link Widget#setInterval(JavaPlugin, long)} to update.
     *
     * @param slots The slots to add.
     * @since 1.1
     */
    public void addSlotsToTrack(final int @NotNull ... slots) {
        Arrays.stream(slots).forEach(this.slots::add);
    }

    /**
     * Sets the handler that handles outside click for this specific widget.
     *
     * @param consumer The consumer to handle the click.
     * @since 1.1
     */
    public void setOutsideHandler(final @Nullable Consumer<@NotNull InventoryClickEvent> consumer) {
        this.outsideHandler = consumer;
    }

    /**
     * Updates exactly one slot and flushes all changes to the live
     * inventory view.
     *
     * @param slot The slot to update.
     * @since 1.0
     */
    public void update(final int slot) {
        if(content[slot] instanceof Styleable<?>) inventory.setItem(slot, ((Styleable<?>) content[slot]).getStyle().getDisplay());
    }

    /**
     * Handles the passed in event.
     *
     * @param event The event to handle.
     * @since 1.0
     */
    public void handle(@NotNull final InventoryClickEvent event) {
        if(event.getRawSlot() >= inventory.getSize() || event.getSlotType() == InventoryType.SlotType.OUTSIDE) {
            if(outsideHandler != null) outsideHandler.accept(event);
        } else if(event.getSlot() < content.length && content[event.getSlot()] != null) {
            content[event.getSlot()].onClick(event);
        }
    }

    /**
     * Opens the inventory for a known human entity.
     *
     * @param entity The entity to open to.
     * @since 1.0
     */
    public void open(@NotNull final HumanEntity entity) {
        entity.openInventory(inventory);
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

}
