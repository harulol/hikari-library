package dev.hawu.plugins.api.gui.transitions;

import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.gui.GuiModel;
import dev.hawu.plugins.api.gui.templates.StaticComponent;
import dev.hawu.plugins.api.gui.templates.TemporaryComponent;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import dev.hawu.plugins.api.adapters.PluginAdapter;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a transition that wipes the interface
 * in a straight line.
 *
 * @since 2.0
 */
public final class LinearWipeTransition extends Transition {

    private static final int WIDTH = 9;
    private final ItemStack itemStack;

    /**
     * Creates a linear wipe transition with the item
     * to transition with.
     *
     * @param item the item
     * @since 2.0
     */
    public LinearWipeTransition(final @NotNull ItemStack item) {
        this.itemStack = item;
    }

    /**
     * Creates a linear wipe transition with black
     * stained glass pane to transition with.
     *
     * @since 2.0
     */
    public LinearWipeTransition() {
        this.itemStack = ItemStackBuilder.of(Material.STAINED_GLASS_PANE)
            .durability((short) 15)
            .name("&a")
            .build();
    }

    @Override
    public void startTransition(final @NotNull HumanEntity entity, final @NotNull GuiModel source, final @NotNull GuiModel destination) {
        checkSource(source);
        if(source.getInventory().getSize() != destination.getInventory().getSize())
            throw new IllegalArgumentException("Inventories during transitions must be of same size.");

        final GuiModel dummy = generateCopy(source);
        destination.updateAll();
        dummy.open(entity);

        // Make the transition.
        final BukkitTask task = new BukkitRunnable() {
            int count = -1;

            public void run() {
                if(count >= 9 + WIDTH) {
                    destination.open(entity);
                    cancel();
                }

                count++;
                if(count >= 9) {
                    return;
                }

                for(int i = count; i < dummy.getInventory().getSize(); i += 9) {
                    final GuiComponent<?> component = new StaticComponent(destination.getInventory().getItem(i));
                    dummy.mount(i, new TemporaryComponent(itemStack, WIDTH, component));
                }
                dummy.updateAll();
            }
        }.runTaskTimer(PluginAdapter.getPlugin(), 1, 1);
        dummy.onClose(task::cancel);
    }

}
