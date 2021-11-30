package dev.hawu.plugins.api.gui.templates;

import dev.hawu.plugins.api.gui.GuiElement;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a GUI element that should never change
 * its rendered status.
 *
 * @since 1.2
 */
public final class StaticElement extends GuiElement<Void> {

    private final ItemStack stack;

    /**
     * Constructs a static GUI element with the provided stack.
     *
     * @param stack The item stack to render as statically.
     * @since 1.2
     */
    public StaticElement(final @Nullable ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public @Nullable ItemStack render() {
        return stack;
    }

}
