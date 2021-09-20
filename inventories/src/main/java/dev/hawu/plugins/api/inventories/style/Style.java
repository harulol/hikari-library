package dev.hawu.plugins.api.inventories.style;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a very primitive style
 * that only has one function to retrieve
 * the currently displaying item.
 *
 * @since 2.0
 */
public interface Style {

    /**
     * The item stack that should be used to display
     * at the moment of invocation.
     *
     * @return The item stack to display.
     * @since 2.0
     */
    @NotNull ItemStack getDisplay();

}
