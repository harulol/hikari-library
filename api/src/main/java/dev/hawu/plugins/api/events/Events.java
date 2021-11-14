package dev.hawu.plugins.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * A class tailored to provide events-related methods.
 *
 * @since 1.0
 */
public final class Events {

    private Events() {}

    /**
     * Creates a new instance of {@link EventSubscriptionBuilder} for building
     * specialized listeners and subscribing to events.
     *
     * @param <T>        The type of the event to handle.
     * @param eventClass The event class to register with.
     * @return A new instance of {@link EventSubscriptionBuilder}.
     * @since 1.0
     */
    @NotNull
    public static <T extends Event> EventSubscriptionBuilder<@NotNull T> newSubscription(final @NotNull Class<T> eventClass) {
        return new EventSubscriptionBuilder<>(eventClass);
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
