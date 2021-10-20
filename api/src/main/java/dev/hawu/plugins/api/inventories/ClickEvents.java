package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.collections.tuples.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Registry for configuring quick handlers that should be run
 * on {@link InventoryClickEvent}s.
 *
 * @since 2.0
 */
public final class ClickEvents implements Listener {

    private static final Map<UUID, Pair<Boolean, Consumer<String>>> chatActions = new HashMap<>(); // The boolean param -> true: async, false: sync
    private static boolean isInit = false;
    private static JavaPlugin plugin;

    private ClickEvents() {}

    /**
     * Initializes and registers the listener so GUIs and
     * text input requests work.
     * <p>
     * Only the first invocation should have any effect.
     * <p>
     * This method should only be invoked by the library's core,
     * not implementations.
     *
     * @param plugin The plugin to initialize with.
     * @since 2.0
     */
    public static void init(@NotNull final JavaPlugin plugin) {
        if(!isInit) Bukkit.getPluginManager().registerEvents(new ClickEvents(), plugin);
        ClickEvents.plugin = plugin;
        isInit = true;
    }

    /**
     * Clears the map that holds actions to be run for
     * text input requests to aid the native GC.
     *
     * @since 2.0
     */
    public static void clear() {
        chatActions.clear();
    }

    /**
     * Specifically requests an input, in a form of a chat message from a player,
     * with the action to be scheduled to run <strong>synchronously</strong>.
     *
     * @param player   The player to request from.
     * @param message  The message to send to the player when requesting.
     * @param function The action to run after the input is received.
     * @since 2.0
     */
    public static void requestInput(@NotNull final Player player, @Nullable final String message, @NotNull final Consumer<@NotNull String> function) {
        player.closeInventory();
        if(message != null) player.sendMessage(Strings.color(message));
        chatActions.put(player.getUniqueId(), new Pair<>(false, function));
    }

    /**
     * Specifically requests an input, in a form of a chat message from a player,
     * with the action to be run <strong>asynchronously</strong>.
     *
     * @param player   The player to request from.
     * @param message  The message to send to the player when requesting.
     * @param function The action to run after the input is received.
     * @since 2.0
     */
    public static void requestInputAsync(@NotNull final Player player, @Nullable final String message, @NotNull final Consumer<@NotNull String> function) {
        player.closeInventory();
        if(message != null) player.sendMessage(Strings.color(message));
        chatActions.put(player.getUniqueId(), new Pair<>(true, function));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onChat(@NotNull final AsyncPlayerChatEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        final Pair<Boolean, Consumer<String>> pair = chatActions.remove(uuid);
        if(pair == null || pair.getFirst() == null || pair.getSecond() == null) return;

        event.setCancelled(true);
        if(pair.getFirst()) pair.getSecond().accept(event.getMessage());
        else Tasks.schedule(plugin, runnable -> pair.getSecond().accept(event.getMessage()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onClick(@NotNull final InventoryClickEvent event) {
        if(event.getInventory().getHolder() instanceof Widget)
            ((Widget) event.getInventory().getHolder()).handle(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onDisable(@NotNull final PluginDisableEvent event) {
        if(event.getPlugin() != plugin) return;

        Bukkit.getOnlinePlayers().stream()
            .filter(p -> p.getOpenInventory().getTopInventory().getHolder() instanceof Widget)
            .forEach(Player::closeInventory);
    }

}
