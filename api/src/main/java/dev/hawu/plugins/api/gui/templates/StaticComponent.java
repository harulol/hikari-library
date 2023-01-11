package dev.hawu.plugins.api.gui.templates;

import dev.hawu.plugins.api.gui.GuiComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a GUI Component that does not depend
 * on state or any hooks, and always renders a static
 * item.
 *
 * @since 2.0
 */
public final class StaticComponent extends GuiComponent<Object> {

    private final ItemStack staticItem;

    /**
     * Creates a static component with the specified static item.
     *
     * @param item the item
     * @since 2.0
     */
    public StaticComponent(final @NotNull ItemStack item) {
        this.staticItem = item;
    }

    @Override
    public @NotNull ItemStack render() {
        return this.staticItem;
    }

}
