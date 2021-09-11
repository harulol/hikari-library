package dev.hawu.plugins.api.events;

import dev.hawu.plugins.api.Constants;
import dev.hawu.plugins.api.events.listener.ClosableListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

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
     * @param handler The handler to be accepted in the method.
     * @param <T>     The type of the event to handle.
     * @return The closable listener after registering.
     * @since 1.0
     */
    @NotNull
    public static <T extends Event> ClosableListener on(@NotNull Consumer<T> handler) {
        final ClosableListener listener = new ClosableListener() {
            @EventHandler
            private void onEvent(final T event) {
                handler.accept(event);
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, Constants.getPlugin());
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
    public static <T extends Event> EventSubscriptionBuilder<T> newSubscription() {
        return new EventSubscriptionBuilder<>();
    }

}
