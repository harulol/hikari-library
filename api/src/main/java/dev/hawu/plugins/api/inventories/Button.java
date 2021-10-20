package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.inventories.style.Style;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a button that can be styled and clickable
 * that may be placed on a widget.
 *
 * @param <S> The type of the style.
 * @since 2.0
 */
public final class Button<S extends Style> implements Clickable, Styleable<S> {

    private final S style;
    private Consumer<InventoryClickEvent> handler = e -> e.setCancelled(true);

    /**
     * Constructs a clickable and styleable button with a specific
     * type of styling.
     *
     * @param styling The style of this button.
     * @since 2.0
     */
    public Button(@NotNull final S styling) {
        this.style = styling;
    }

    @NotNull
    @Override
    public S getStyle() {
        return style;
    }

    /**
     * Overrides the handler field of this button using
     * the provided parameter value.
     *
     * @param consumer The consumer to override with.
     * @since 2.0
     */
    public void setHandler(@NotNull final Consumer<@NotNull InventoryClickEvent> consumer) {
        this.handler = consumer;
    }

    /**
     * If the handler field is uninitialized, initialize it with the provided
     * parameter, else chain the handler using {@link Consumer#andThen(Consumer)}.
     *
     * @param other The other consumer to chain or initialize with.
     * @since 2.0
     */
    public void andThen(@NotNull final Consumer<@NotNull InventoryClickEvent> other) {
        if(this.handler == null) this.handler = other;
        else this.handler = this.handler.andThen(other);
    }

    /**
     * Calls and handles the passed in event. This should only be
     * invoked when the click registers on this specific button.
     *
     * @param event The event to handle.
     * @since 2.0
     */
    @Override
    public void onClick(final @NotNull InventoryClickEvent event) {
        if(this.handler != null) this.handler.accept(event);
    }

}
