package dev.hawu.plugins.api.events.listener;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.Closeable;

/**
 * Represents an extension of the {@link Listener} interface that
 * can be closed.
 *
 * @since 1.2
 */
public final class ClosableListener implements Listener, Closeable {

    /**
     * Closes this listener by unregistering everything
     * from the handler list.
     *
     * @since 1.2
     */
    public void close() {
        HandlerList.unregisterAll(this);
    }

}
