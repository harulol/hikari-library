package dev.hawu.plugins.api.beta.gui.transitions;

import dev.hawu.plugins.api.beta.gui.GuiModel;
import dev.hawu.plugins.api.beta.gui.templates.StaticComponent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a GUI transition.
 *
 * @since 2.0
 */
public abstract class Transition {

    /**
     * Generates a static copy of a GUI model.
     * <p>
     * This copy does not have any interactivity.
     *
     * @param source the source
     * @return the static copy
     * @since 2.0
     */
    public final GuiModel generateCopy(final @NotNull GuiModel source) {
        final GuiModel dummy;
        if(!source.getInventory().getType().name().contains("CHEST"))
            dummy = new GuiModel(source.getInventory().getType(), source.getInventory().getTitle());
        else dummy = new GuiModel(source.getInventory().getSize(), source.getInventory().getTitle());

        final ItemStack[] contents = source.getInventory().getContents();
        for(int i = 0; i < contents.length; i++) {
            dummy.mount(i, new StaticComponent(contents[i]));
        }

        return dummy;
    }

    /**
     * Runs a check to make sure the source model is of chest type.
     *
     * @param source the source
     * @since 2.0
     */
    public final void checkSource(final @NotNull GuiModel source) {
        if(source.getInventory().getSize() % 9 != 0 || !source.getInventory().getType().name().contains("CHEST"))
            throw new IllegalArgumentException("Source inventory must be a chest inventory.");
    }

    /**
     * Starts the transition from the source model to the destination.
     *
     * @param entity      the entity to animate to
     * @param source      the source
     * @param destination the destination
     * @since 2.0
     */
    public abstract void startTransition(final @NotNull HumanEntity entity, final @NotNull GuiModel source, final @NotNull GuiModel destination);

}
