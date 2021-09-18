package dev.hawu.plugins.api.inventories;

import dev.hawu.plugins.api.inventories.item.ItemStackBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents a clickable button placeable in widgets
 * and can be interacted.
 *
 * @since 1.0
 */
public class Button<T> {

    private T value;
    private ItemStack base;
    private BiConsumer<T, InventoryClickEvent> action;

    /**
     * Constructs an empty button with only an item stack to be
     * displayed.
     *
     * @param b The item stack to display as.
     * @since 1.0
     */
    public Button(@NotNull final ItemStack b) {
        this.base = b;
    }

    /**
     * Applies a function to transform without mutating the field that holds
     * the displayable item.
     *
     * @param consumer The function to apply.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public Button<T> transformBase(@NotNull final Consumer<ItemStack> consumer) {
        consumer.accept(base);
        return this;
    }

    /**
     * Overrides the base field in this button with the newly provided item stack.
     *
     * @param item The item to override with.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public Button<T> replaceBase(@NotNull final ItemStack item) {
        this.base = item;
        return this;
    }

    /**
     * Overrides the action field in this button with the newly provided bi-consumer.
     *
     * @param consumer The consumer to override with.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public Button<T> setAction(@NotNull final BiConsumer<T, InventoryClickEvent> consumer) {
        this.action = consumer;
        return this;
    }

    /**
     * Chains the provided consumer to be executed right after the current action consumer
     * by using {@link BiConsumer#andThen(BiConsumer)}.
     *
     * @param other The other consumer to chain.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public Button<T> andThen(@NotNull final BiConsumer<T, InventoryClickEvent> other) {
        this.action = this.action.andThen(other);
        return this;
    }

    /**
     * Attempts to modify the displaying item stack by using a dedicated
     * {@link ItemStackBuilder}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemStackBuilder withBuilder() {
        return new ItemStackBuilder(base);
    }

    /**
     * Retrieves the displayed item at the moment of invocation.
     *
     * @return The base item field.
     * @since 1.0
     */
    @NotNull
    public ItemStack getDisplay() {
        return base;
    }

    /**
     * Gets the value stored within this button.
     *
     * @return The value of type {@link T} in this button.
     * @since 1.0
     */
    @Nullable
    public T getValue() {
        return value;
    }

    /**
     * Sets the value of this button.
     *
     * @param value The new value of this button.
     * @since 1.0
     */
    public void setValue(@Nullable final T value) {
        this.value = value;
    }

    /**
     * Attempts to handle the provided event, using the current value.
     * <p>
     * This is open for internal usage.
     *
     * @param event The event to handle.
     * @since 1.0
     */
    public void handle(@NotNull final InventoryClickEvent event) {
        action.accept(value, event);
    }

}
