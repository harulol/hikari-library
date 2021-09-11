package dev.hawu.plugins.api.events.listener;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * Represents an extended listener that can
 * be closed.
 *
 * @since 1.0
 */
public interface ClosableListener extends Listener {

    /**
     * Closes this current listener by unregistering all handlers
     * from the {@link HandlerList}.
     *
     * @since 1.0
     */
    default void close() {
        HandlerList.unregisterAll(this);
    }

}
