package dev.hawu.plugins.api.inventories.style;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a style that has two states and can be toggled
 * from implementations.
 * <p>
 * This can be used as a styling for binary options, with 2 states.
 * <p>
 * Both states are immutable, but not frozen. Transformers and functions
 * may apply changes to them, but the fields never mutate.
 *
 * @since 1.0
 */
public final class ToggleableStyle implements Style {

    private final ItemStack enabled;
    private final ItemStack disabled;
    private boolean isEnabled;

    /**
     * Constructs a toggleable style with stacks to be displayed when
     * this style is toggled.
     *
     * @param enabled  The item to display when the state is on.
     * @param disabled The item to display when the state is off.
     * @since 1.0
     */
    public ToggleableStyle(@NotNull final ItemStack enabled, @NotNull final ItemStack disabled) {
        this.enabled = enabled;
        this.disabled = disabled;
    }

    /**
     * Toggles the state of this style, marking it as on if it was
     * previously off and vice versa.
     *
     * @since 1.0
     */
    public void toggle() {
        isEnabled = !isEnabled;
    }

    /**
     * Checks the state of this style.
     *
     * @return True if it is on, false otherwise.
     * @since 1.0
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Applies a transformer to the item to be displayed
     * when the state is on.
     *
     * @param consumer The transformer to apply.
     * @since 1.0
     */
    public void transformEnabledItem(@NotNull final Consumer<ItemStack> consumer) {
        consumer.accept(enabled);
    }

    /**
     * Applies a transformer to the item to be displayed
     * when the state is off.
     *
     * @param consumer The transformer to apply.
     * @since 1.0
     */
    public void transformDisabledItem(@NotNull final Consumer<ItemStack> consumer) {
        consumer.accept(disabled);
    }

    @Override
    public @NotNull ItemStack getDisplay() {
        return isEnabled ? enabled : disabled;
    }

}
