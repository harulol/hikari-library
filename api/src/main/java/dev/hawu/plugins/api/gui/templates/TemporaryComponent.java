package dev.hawu.plugins.api.gui.templates;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.misc.PluginAdapter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a component that is only mounted temporarily.
 *
 * @since 2.0
 */
public final class TemporaryComponent extends GuiComponent<Object> {

    private final ItemStack item;
    private final long ticks;
    private final GuiComponent<?> partner;

    /**
     * Constructs a component that after a while, swaps
     * with the partner component.
     *
     * @param item    the item to show
     * @param ticks   the ticks until swap
     * @param partner the swap partner
     * @since 2.0
     */
    public TemporaryComponent(final @NotNull ItemStack item, final long ticks, final @NotNull GuiComponent<?> partner) {
        this.item = item;
        this.ticks = ticks;
        this.partner = partner;
    }

    @Override
    public void componentDidMount() {
        Tasks.scheduleLater(PluginAdapter.getPlugin(), this.ticks, run -> this.swap(partner));
    }

    @Override
    public @NotNull ItemStack render() {
        return item;
    }

}
