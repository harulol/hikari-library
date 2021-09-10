package dev.hawu.plugins.api.events.listener;

import dev.hawu.plugins.api.Constants;
import dev.hawu.plugins.api.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a specialized listener that handles one event
 * that may have filters, expirations and hooks.
 * @param <T> The event to handle.
 * @since 1.0
 */
public final class SpecializedListener<T extends Event> implements ClosableListener {

    private final int invocationExpiry;
    private int invocationsCount;
    private final double timedExpiry;
    private final double registration;

    private final Predicate<T> predicate;
    private final boolean countsIfFiltered;

    private final Consumer<T> handler;
    private final BiConsumer<T, ? super Exception> onFailHook;
    private final Runnable onUnregisterHook;

    /**
     * Constructs a specialized listener.
     * @param invocationExpiry The amount of invocation times until it is unregistered.
     * @param timedExpiry The amount of time until it is unregistered.
     * @param predicate The predicate events have to pass to be passed on to the handler.
     * @param countsIfFiltered Whether to increment the invocation count even if handler wasn't called.
     * @param onFailHook The hook that handles the exception if the handler failed.
     * @param handler The handler that handles the event.
     * @param onUnregisterHook The hook that is run after this listener has been unregistered.
     * @since 1.0
     */
    public SpecializedListener(final int invocationExpiry, final double timedExpiry,
                               @Nullable final Predicate<T> predicate, final boolean countsIfFiltered,
                               @Nullable final BiConsumer<T, ? super Exception> onFailHook,
                               @Nullable final Consumer<T> handler,
                               @Nullable final Runnable onUnregisterHook) {
        this.invocationExpiry = invocationExpiry;
        this.timedExpiry = timedExpiry;
        this.predicate = predicate;
        this.countsIfFiltered = countsIfFiltered;
        this.onFailHook = onFailHook;
        this.handler = handler;
        this.onUnregisterHook = onUnregisterHook;

        this.invocationsCount = 0;
        this.registration = System.currentTimeMillis();

        Bukkit.getServer().getPluginManager().registerEvents(this, Constants.getPlugin());
        if(timedExpiry > 0) Tasks.scheduleLater((long) timedExpiry, runnable -> this.close());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        ClosableListener.super.close();
        onUnregisterHook.run();
    }

    @EventHandler
    private void onEvent(final T event) {
        if((timedExpiry > 0 && registration + timedExpiry <= System.currentTimeMillis())
        || (invocationExpiry > 0 && invocationsCount >= invocationExpiry)) {
            close();
            return;
        }
        if(predicate != null && !predicate.test(event)) {
            if(countsIfFiltered) invocationsCount++;
            return;
        }

        try {
            invocationsCount++;
            handler.accept(event);
        } catch(final Exception exception) {
            if(onFailHook != null) onFailHook.accept(event, exception);
        }
    }

}
