package dev.hawu.plugins.api.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * The click events that should provide miscellaneous
 * stuff to make GUIs work.
 *
 * @since 1.2
 */
public final class GuiClickEvents implements Listener {

    private static final GuiClickEvents INSTANCE = new GuiClickEvents();

    private GuiClickEvents() {}

    /**
     * Retrieves the singleton instance of this class.
     *
     * @return the singleton instance of this class.
     * @since 1.2
     */
    @NotNull
    public static GuiClickEvents getInstance() {
        return INSTANCE;
    }

    /**
     * Safely closes the inventory view of a player if they
     * have a GuiModel open.
     *
     * @param player The player whose inventory to close.
     * @since 1.2
     */
    public static void safelyClose(final @NotNull Player player) {
        final InventoryView openInv = player.getOpenInventory();
        if(openInv == null) return;
        final Inventory top = openInv.getTopInventory();
        if(top != null && top.getHolder() instanceof GuiModel) {
            player.closeInventory();
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    private void onClick(final @NotNull InventoryClickEvent event) {
        if(event.getInventory().getHolder() instanceof GuiModel) {
            ((GuiModel) event.getInventory().getHolder()).handleClick(event);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    private void onDisable(final @NotNull PluginDisableEvent event) {
        final List<String> dependencies = new ArrayList<>(event.getPlugin().getDescription().getDepend());
        dependencies.addAll(event.getPlugin().getDescription().getSoftDepend());

        if(dependencies.contains("HikariLibrary")) {
            Bukkit.getOnlinePlayers().forEach(GuiClickEvents::safelyClose);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    private void onClose(final @NotNull InventoryCloseEvent event) {
        if(event.getInventory().getHolder() instanceof GuiModel) {
            ((GuiModel) event.getInventory().getHolder()).handleClose();
        }
    }

}
