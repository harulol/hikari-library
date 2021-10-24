package dev.hawu.plugins.api.inventories;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an element that can be clicked in a widget.
 *
 * @since 1.0
 */
public interface Clickable {

    /**
     * Handles the passed in event.
     *
     * @param event The event to handle.
     * @since 1.0
     */
    void onClick(@NotNull final InventoryClickEvent event);

}
