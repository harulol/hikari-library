package dev.hawu.plugins.api.events;

import dev.hawu.plugins.api.events.listener.SpecializedListener;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a builder to build a subscription to an event
 * using a specialized listener.
 * @param <T> The event to listen to.
 * @since 1.0
 */
public final class EventSubscriptionBuilder<T extends Event> {

    private int invocationExpiry = -1;
    private double timedExpiry = -1.0;
    private Predicate<T> predicate;
    private boolean countsIfFiltered;
    private BiConsumer<T, ? super Exception> onFailHook;
    private Consumer<T> handler;
    private Runnable onUnregisterHook;

    EventSubscriptionBuilder() {}

    /**
     * Specifies that the listener should expire and unregister
     * itself after a certain amount of time.
     * @param duration Time in mills until expiration.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> expiresAfterTime(final double duration) {
        timedExpiry = duration;
        return this;
    }

    /**
     * Specifies that the listener should expire after a certain
     * number of invocations.
     * @param amount Amount of invocation times until expiration.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> expiresAfterInvocations(final int amount) {
        invocationExpiry = amount;
        return this;
    }

    /**
     * Appends to the current listener's filter.
     * @param other The other predicate.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> filter(@NotNull final Predicate<T> other) {
        if(predicate == null) predicate = other;
        else predicate = predicate.and(other);
        return this;
    }

    /**
     * Overrides the value of this listener's predicate using the provided value.
     * @param predicate The predicate to override with.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> predicate(@Nullable final Predicate<T> predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * Overrides the value of this listener's handler using the provided value.
     * @param handler The handler of this listener.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> handler(@NotNull final Consumer<T> handler) {
        this.handler = handler;
        return this;
    }

    /**
     * Appends the provided handler to be executed right after the current handler,
     * and overrides it if the current one is {@code null}.
     * @param other The other handler to append.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> then(@NotNull final Consumer<T> other) {
        this.handler = handler.andThen(other);
        return this;
    }

    /**
     * Specifies that the specialized listeners should increment the invocation
     * counter even if the handler does not get called.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> countsIfFiltered() {
        this.countsIfFiltered = true;
        return this;
    }

    /**
     * Sets the specialized listener's hook to be run when the handler
     * throws an {@link Exception}.
     * @param hook The hook to override with.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> onFail(@NotNull final BiConsumer<T, ? super Exception> hook) {
        this.onFailHook = hook;
        return this;
    }

    /**
     * Sets the specialized listener's hook to be run when it is
     * closed and unregistered.
     * @param hook The hook to override with.
     * @return The same receiver.
     * @since 1.0
     */
    public EventSubscriptionBuilder<T> onUnregister(@NotNull final Runnable hook) {
        this.onUnregisterHook = hook;
        return this;
    }

    /**
     * Builds and retrieves the {@link SpecializedListener} instance with
     * all the provided parameters.
     * @return A newly created {@link SpecializedListener}.
     * @since 1.0
     */
    public SpecializedListener<T> build() {
        return new SpecializedListener<>(invocationExpiry, timedExpiry, predicate, countsIfFiltered, onFailHook, handler, onUnregisterHook);
    }

}
