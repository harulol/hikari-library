package dev.hawu.plugins.api.gui;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.events.Events;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * The click events that should provide miscellaneous
 * stuff to make GUIs work.
 *
 * @since 1.2
 */
public final class GuiClickEvents implements Listener {

    private static final GuiClickEvents INSTANCE = new GuiClickEvents();
    private static final Map<UUID, Pair<Boolean, Consumer<String>>> textInputs = new HashMap<>(); // UUID -> <async, callback>

    private static JavaPlugin plugin;

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
     * Initializes this class with the plugin
     * to be used for scheduling tasks.
     *
     * @param pl The plugin to register.
     * @since 1.2
     */
    public static void initialize(final @NotNull JavaPlugin pl) {
        if(plugin == null) {
            plugin = pl;
            Events.registerEvents(pl, getInstance());
        }
    }

    /**
     * Cleans up the associated maps.
     *
     * @since 1.2
     */
    public static void onDisable() {
        textInputs.clear();
    }

    /**
     * Requests the player to input a text message
     * to pass in the callback.
     *
     * @param player   The player to ask.
     * @param callback The function to handle the input.
     * @since 1.2
     */
    public static void requestTextInput(final @NotNull Player player, final @NotNull Consumer<@NotNull String> callback) {
        textInputs.put(player.getUniqueId(), new Pair<>(false, callback));
    }

    /**
     * Requests the player to input a text message
     * to pass in the callback asynchronously.
     * <p>
     * Using the Bukkit API in async calls may
     * be problematic according to the documentation.
     *
     * @param player   The player to ask.
     * @param callback The function to handle the input.
     * @since 1.2
     */
    public static void requestTextInputAsync(final @NotNull Player player, final @NotNull Consumer<@NotNull String> callback) {
        textInputs.put(player.getUniqueId(), new Pair<>(true, callback));
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
    @EventHandler(priority = EventPriority.LOWEST)
    private void onChat(final @NotNull AsyncPlayerChatEvent event) {
        if(!textInputs.containsKey(event.getPlayer().getUniqueId())) return;

        event.setCancelled(true);
        final Pair<Boolean, Consumer<String>> pair = textInputs.get(event.getPlayer().getUniqueId());
        if(pair.getFirst()) {
            Tasks.scheduleAsync(plugin, runnable -> pair.getSecond().accept(event.getMessage()));
        } else {
            Tasks.schedule(plugin, runnable -> pair.getSecond().accept(event.getMessage()));
        }
        textInputs.remove(event.getPlayer().getUniqueId());
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST)
    private void onClick(final @NotNull InventoryClickEvent event) {
        if(event.getInventory().getHolder() instanceof GuiModel) {
            final GuiModel model = (GuiModel) event.getInventory().getHolder();
            model.handleClick(event);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST)
    private void onDrag(final @NotNull InventoryDragEvent event) {
        if(event.getInventory().getHolder() instanceof GuiModel) {
            final GuiModel model = (GuiModel) event.getInventory().getHolder();
            model.handleDrag(event);
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
