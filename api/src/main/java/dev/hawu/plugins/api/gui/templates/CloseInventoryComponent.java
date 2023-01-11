package dev.hawu.plugins.api.gui.templates;

import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds a GUI Component that closes the inventory
 * on click.
 *
 * @since 2.0
 */
public final class CloseInventoryComponent extends GuiComponent<Object> {

    private final ItemStack staticItem;

    /**
     * Creates a close-inventory component that has the static item
     * as a barrier with the name Close in red.
     *
     * @since 2.0
     */
    public CloseInventoryComponent() {
        this(ItemStackBuilder.of(Material.BARRIER)
            .name("&c&lClose")
            .lore("&7Close the menu")
            .build());
    }

    /**
     * Creates a close-inventory component with the specified static item.
     *
     * @param item the item
     * @since 2.0
     */
    public CloseInventoryComponent(final @NotNull ItemStack item) {
        this.staticItem = item;
    }

    @Override
    public void handleClick(final @NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        event.getWhoClicked().closeInventory();
    }

    @Override
    public @Nullable ItemStack render() {
        return staticItem;
    }

}
