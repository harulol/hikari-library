package dev.hawu.plugins.api.gui.templates;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * An abstract implementation for a component that updates every tick
 * for smooth animation.
 *
 * @since 2.0
 */
public abstract class AnimatedComponent extends GuiComponent<Integer> {

    private final long period;
    private final long delay;
    private final ItemStack template;
    private BukkitTask task;

    /**
     * Constructs an animated component with custom delay and intervals.
     *
     * @param delay    the delay in ticks
     * @param period   the interval in ticks
     * @param template the template for the item
     * @since 2.0
     */
    public AnimatedComponent(final long delay, final long period, final @NotNull ItemStack template) {
        this.delay = delay;
        this.period = period;
        this.template = template;
    }

    /**
     * Constructs an animated component with 0 ticks delay and 1 tick between
     * intervals.
     *
     * @param template the template for the item
     * @since 2.0
     */
    public AnimatedComponent(final @NotNull ItemStack template) {
        this(0, 1, template);
    }

    /**
     * Open for implementation to get the display name for the current state.
     *
     * @return the display name
     * @since 2.0
     */
    @NotNull
    public abstract String getDisplayName();

    /**
     * Open for implementation to get the lore for the current state.
     *
     * @return the lore
     * @since 2.0
     */
    @NotNull
    public abstract List<String> getLore();

    @Override
    public final void componentDidMount() {
        if(task != null) task.cancel();
        task = Tasks.run(() -> setState(state -> state + 1)).async(true).period(period).delay(delay).run();
    }

    @Override
    public final void componentWillUnmount() {
        if(task != null) task.cancel();
        task = null;
    }

    @Override
    public final ItemStack render() {
        return ItemStackBuilder.from(template)
            .name(getDisplayName())
            .lore(getLore())
            .build();
    }

}
