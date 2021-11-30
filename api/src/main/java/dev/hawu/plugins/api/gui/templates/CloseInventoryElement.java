package dev.hawu.plugins.api.gui.templates;

import dev.hawu.plugins.api.gui.GuiElement;
import dev.hawu.plugins.api.inventories.Inventories;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A concrete implementation of a {@link GuiElement} that closes
 * the clicker's inventory on click.
 *
 * @since 1.2
 */
public final class CloseInventoryElement extends GuiElement<Void> {

    private final boolean safely;
    private final HumanEntity who;
    private final ItemStack itemStack;

    /**
     * Constructs a basic close button with all default values.
     *
     * @since 1.2
     */
    public CloseInventoryElement() {
        this(true);
    }

    /**
     * Constructs a basic close button with most default values.
     *
     * @param safely Whether to close the inventory safely according to the documentation.
     * @since 1.2
     */
    public CloseInventoryElement(final boolean safely) {
        this(safely, null);
    }

    /**
     * Constructs a basic close button with some default values.
     *
     * @param safely Whether to close the inventory safely according to the documentation.
     * @param who    The {@link HumanEntity} to close the inventory.
     * @since 1.2
     */
    public CloseInventoryElement(final boolean safely, final @Nullable HumanEntity who) {
        this(safely, who, ItemStackBuilder.of(Material.BARRIER).name("&c&lClose").lore("&7Closes this menu").build());
    }

    /**
     * Constructs a basic close button with one default value.
     *
     * @param safely Whether to close the inventory safely according to the documentation.
     * @param who    The {@link HumanEntity} to close the inventory.
     * @param stack  The {@link ItemStack} to render as statically.
     */
    public CloseInventoryElement(final boolean safely, final @Nullable HumanEntity who, final @NotNull ItemStack stack) {
        this.safely = safely;
        this.who = who;
        this.itemStack = stack;
    }

    @Override
    public void handleClick(final @NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        final HumanEntity entity = who == null ? event.getWhoClicked() : who;
        if(safely) Inventories.safeCloseInventory(entity);
        else entity.closeInventory();
    }

    @Override
    public @Nullable ItemStack render() {
        return itemStack;
    }

}
