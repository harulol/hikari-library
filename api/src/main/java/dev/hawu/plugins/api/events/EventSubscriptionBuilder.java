package dev.hawu.plugins.api.events;

import dev.hawu.plugins.api.TimeConversions;
import dev.hawu.plugins.api.events.listener.ClosableListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a temporary or a permanent subscription
 * to a Bukkit event.
 *
 * @param <T> The type of the event.
 * @since 1.2
 */
public final class EventSubscriptionBuilder<T extends Event> {

    private final Class<T> eventClass;
    private EventPriority priority = EventPriority.NORMAL;
    private boolean ignoreCancelled = false;

    private BiConsumer<ClosableListener, T> consumer;
    private Predicate<T> predicate;
    private boolean countsIfFiltered;
    private long expiryInvocations = -1;
    private long expiryTime = -1;

    /**
     * Constructs a new builder to build an event subscription.
     *
     * @param eventClass The class of the event.
     * @since 1.2
     */
    public EventSubscriptionBuilder(final @NotNull Class<T> eventClass) {
        this.eventClass = eventClass;
    }

    /**
     * Sets the priority for this event subscription.
     *
     * @param priority The priority for the listener.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> priority(final @NotNull EventPriority priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Makes the listener handler still run even if the event is cancelled.
     *
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> ignoreCancelled() {
        this.ignoreCancelled = true;
        return this;
    }

    /**
     * Sets the handler for this event subscription.
     *
     * @param consumer The handler for the event.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> handler(final @NotNull Consumer<@NotNull T> consumer) {
        this.consumer = (listener, event) -> consumer.accept(event);
        return this;
    }

    /**
     * Sets the predicate for this event subscription
     * with two parameters.
     *
     * @param consumer the handler for the event
     * @return the same builder
     * @since 1.5
     */
    @NotNull
    public EventSubscriptionBuilder<T> openHandler(final @NotNull BiConsumer<@NotNull ClosableListener, @NotNull T> consumer) {
        this.consumer = consumer;
        return this;
    }

    /**
     * Appends the provided consumer to the handler
     * of this event subscription.
     *
     * @param consumer The consumer to append.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> andThen(final @NotNull Consumer<@NotNull T> consumer) {
        if(this.consumer == null) handler(consumer);
        else this.consumer = this.consumer.andThen((listener, event) -> consumer.accept(event));
        return this;
    }

    /**
     * Makes it so that this subscription's counter still increments
     * even if the predicate tested false.
     *
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> countsIfFiltered() {
        this.countsIfFiltered = true;
        return this;
    }

    /**
     * Sets the filter for this subscription or append
     * to the existing predicate using {@link Predicate#and(Predicate)} function.
     *
     * @param predicate The predicate to set or append.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> filter(final @NotNull Predicate<T> predicate) {
        if(this.predicate == null) this.predicate = predicate;
        else this.predicate = this.predicate.and(predicate);
        return this;
    }

    /**
     * Makes it so this subscription automatically closes itself
     * after a certain amount of calls to the handler.
     * <p>
     * The invocation counter increments before the handler is called.
     *
     * @param amount The amount of calls before this subscription closes.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> expiresAfterInvocations(final long amount) {
        this.expiryInvocations = amount;
        return this;
    }

    /**
     * Makes it so this subscription expires after a certain number of milliseconds.
     *
     * @param time The time before this subscription closes.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> expiresAfter(final long time) {
        this.expiryTime = time;
        return this;
    }

    /**
     * Makes it so this subscription expires after a certain amount of time,
     * parsed using {@link TimeConversions#convertToMillis(String)}.
     *
     * @param time The time before this subscription closes.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public EventSubscriptionBuilder<T> expiresAfter(final @NotNull String time) {
        this.expiryTime = (long) TimeConversions.convertToMillis(time);
        return this;
    }

    /**
     * Builds the subscription using the provided values
     * and returns a closable listener.
     *
     * @param plugin The plugin to register to.
     * @return Said closable listener.
     * @since 1.2
     */
    @NotNull
    public ClosableListener build(final @NotNull JavaPlugin plugin) {
        final ClosableListener listener = new ClosableListener();
        final AtomicInteger count = new AtomicInteger();
        final long buildTime = System.currentTimeMillis();
        Bukkit.getPluginManager().registerEvent(eventClass, listener, priority, (l, event) -> {
            if(!eventClass.isInstance(event)) return;
            T casted = eventClass.cast(event);

            if((expiryTime > 0 && System.currentTimeMillis() - buildTime > expiryTime) || (expiryInvocations > 0 && count.get() == expiryInvocations)) {
                listener.close();
                return;
            }

            if(predicate != null && !predicate.test(casted)) {
                if(countsIfFiltered) count.getAndIncrement();
                return;
            }

            if(count.incrementAndGet() == expiryInvocations)
                listener.close();
            this.consumer.accept(listener, casted);
        }, plugin, ignoreCancelled);
        return listener;
    }

}
