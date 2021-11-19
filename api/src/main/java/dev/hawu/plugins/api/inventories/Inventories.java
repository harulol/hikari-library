package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.Tasks;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Apparently, invocations of methods relating to inventories within
 * event handlers that handle {@link org.bukkit.event.inventory.InventoryClickEvent}
 * are not safe, so this class provides a "safer" way of invoking said methods.
 * <p>
 * Keep in mind that these methods are only better than its direct counterpart
 * if these are invoked within said event handlers.
 * <p>
 * This has no other special effect.
 *
 * @since 1.2
 */
public final class Inventories {

    private static JavaPlugin plugin;

    private Inventories() {}

    /**
     * Sets the plugin
     * <p>
     * This is not public API.
     *
     * @param pl The plugin instance.
     * @since 1.2
     */
    @Internal
    public static void setPlugin(final @NotNull JavaPlugin pl) {
        if(plugin == null) plugin = pl;
    }

    /**
     * Safely close a player's inventory one tick after the event.
     *
     * @param entity The entity to close.
     * @since 1.2
     */
    public static void safeCloseInventory(final @NotNull HumanEntity entity) {
        Tasks.schedule(plugin, runnable -> entity.closeInventory());
    }

    /**
     * Safely closes an inventory view.
     *
     * @param view The view to close.
     * @since 1.2
     */
    public static void safeClose(final @NotNull InventoryView view) {
        Tasks.schedule(plugin, runnable -> view.close());
    }

    /**
     * Safely opens an inventory to a human entity and retrieves
     * the returned value via a future instance.
     *
     * @param entity    The entity to open to.
     * @param inventory The inventory to open.
     * @return Said future instance.
     * @since 1.2
     */
    @NotNull
    public static CompletableFuture<@NotNull InventoryView> safeOpenInventory(final @NotNull HumanEntity entity, final @NotNull Inventory inventory) {
        final CompletableFuture<InventoryView> future = new CompletableFuture<>();
        Tasks.schedule(plugin, runnable -> future.complete(entity.openInventory(inventory)));
        return future;
    }

    /**
     * Safely opens an inventory view to a human entity.
     *
     * @param entity The entity to open to.
     * @param view   The view to open.
     * @since 1.2
     */
    public static void safeOpenInventory(final @NotNull HumanEntity entity, final @NotNull InventoryView view) {
        Tasks.schedule(plugin, runnable -> entity.openInventory(view));
    }

    /**
     * Safely attempts to open a workbench inventory
     * to a human entity at a location, whether forcefully or not,
     * and retrieves the returned value via a future instance.
     *
     * @param entity   The entity to open to.
     * @param location The location to open.
     * @param force    Whether to force the opening.
     * @return Said future instance.
     * @since 1.2
     */
    @NotNull
    public static CompletableFuture<@Nullable InventoryView> openWorkbench(final @NotNull HumanEntity entity, final @NotNull Location location, final boolean force) {
        final CompletableFuture<InventoryView> future = new CompletableFuture<>();
        Tasks.schedule(plugin, runnable -> future.complete(entity.openWorkbench(location, force)));
        return future;
    }

    /**
     * Safely opens an enchanting inventory to a human entity at a location,
     * whether forcefully or not, and retrieves the returned value via a future instance.
     *
     * @param entity   The entity to open to.
     * @param location The location to open.
     * @param force    Whether to force the opening.
     * @return Said future instance.
     * @since 1.2
     */
    @NotNull
    public static CompletableFuture<@Nullable InventoryView> openEnchanting(final @NotNull HumanEntity entity, final @NotNull Location location, final boolean force) {
        final CompletableFuture<InventoryView> future = new CompletableFuture<>();
        Tasks.schedule(plugin, runnable -> future.complete(entity.openEnchanting(location, force)));
        return future;
    }

}
