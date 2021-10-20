package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.inventories.style.Style;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

/**
 * Represents a clickable and styleable button that has a value,
 * and may be used to transform styles and handlers.
 *
 * @param <T> The type of the value.
 * @param <S> The type of the style.
 * @since 2.0
 */
public final class ValuedButton<T, S extends Style> implements Clickable, Styleable<S>, Valuable<T> {

    private final S style;
    private T value;
    private BiConsumer<T, InventoryClickEvent> consumer;

    /**
     * Constructs a valued button, with initial value and style.
     *
     * @param initialValue The value to init with.
     * @param style        The style to init with.
     * @since 2.0
     */
    public ValuedButton(@Nullable final T initialValue, @NotNull final S style) {
        this.value = initialValue;
        this.style = style;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The value of this button, at the moment of invocation
     * will be used in the handler.
     *
     * @param event The event to handle.
     * @since 2.0
     */
    @Override
    public void onClick(final @NotNull InventoryClickEvent event) {
        if(this.consumer != null) consumer.accept(value, event);
    }

    @Override
    public @NotNull S getStyle() {
        return style;
    }

    /**
     * Sets the handler of this button, which is a {@link BiConsumer} that takes
     * the type of the value and the {@link InventoryClickEvent}.
     *
     * @param consumer The handler of this button.
     * @since 2.0
     */
    public void setHandler(@NotNull final BiConsumer<@Nullable T, @NotNull InventoryClickEvent> consumer) {
        this.consumer = consumer;
    }

    /**
     * Chains the following consumer to be run after the current handler,
     * or sets the field if it's not yet initialized.
     *
     * @param other The other consumer to chain with.
     * @since 2.0
     */
    public void andThen(@NotNull final BiConsumer<@Nullable T, @NotNull InventoryClickEvent> other) {
        if(this.consumer == null) this.consumer = other;
        else this.consumer = this.consumer.andThen(other);
    }

    @Override
    @Nullable
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(@Nullable final T value) {
        this.value = value;
    }

}
