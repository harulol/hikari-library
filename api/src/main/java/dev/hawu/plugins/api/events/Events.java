package dev.hawu.plugins.api.events;

import dev.hawu.plugins.api.events.listener.ClosableListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * A class tailored to provide events-related methods.
 *
 * @since 1.0
 */
public final class Events {

    private Events() {}

    /**
     * Registers a very simple {@link ClosableListener} with one event handler
     * that handles the provided event of type T.
     *
     * @param plugin  The plugin to register with.
     * @param handler The handler to be accepted in the method.
     * @param <T>     The type of the event to handle.
     * @return The closable listener after registering.
     * @since 1.0
     */
    @NotNull
    public static <T extends Event> ClosableListener on(final @NotNull JavaPlugin plugin, @NotNull Consumer<@NotNull T> handler) {
        final ClosableListener listener = new ClosableListener() {
            @EventHandler
            private void onEvent(final T event) {
                handler.accept(event);
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, plugin);
        return listener;
    }

    /**
     * Creates a new instance of {@link EventSubscriptionBuilder} for building
     * specialized listeners and subscribing to events.
     *
     * @param <T> The type of the event to handle.
     * @return A new instance of {@link EventSubscriptionBuilder}.
     * @since 1.0
     */
    @NotNull
    public static <T extends Event> EventSubscriptionBuilder<@NotNull T> newSubscription() {
        return new EventSubscriptionBuilder<>();
    }

    /**
     * Registers multiple listeners to a single plugin instance within
     * one method invocation.
     *
     * @param plugin    The plugin to register with.
     * @param listeners The array of listeners to register.
     * @since 1.1
     */
    public static void registerEvents(final @NotNull JavaPlugin plugin, final @NotNull Listener @NotNull ... listeners) {
        Arrays.stream(listeners).forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }

}
