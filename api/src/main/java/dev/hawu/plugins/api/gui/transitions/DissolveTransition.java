package dev.hawu.plugins.api.gui.transitions;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.gui.GuiModel;
import dev.hawu.plugins.api.gui.templates.StaticComponent;
import dev.hawu.plugins.api.gui.templates.TemporaryComponent;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import dev.hawu.plugins.api.adapters.PluginAdapter;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a transition that randomly places item until
 * everything is visible.
 *
 * @since 2.0
 */
public final class DissolveTransition extends Transition {

    private final ItemStack item;
    private final int batches;

    /**
     * Creates a dissolve transition with the item specified.
     *
     * @param item    the item.
     * @param batches amount of items to dissolve per tick.
     * @since 2.0
     */
    public DissolveTransition(final @NotNull ItemStack item, final int batches) {
        this.item = item;
        this.batches = batches;
    }

    /**
     * Creates a dissolve transition with the item as black stained
     * glass with no name.
     *
     * @param batches amount of items to dissolve per tick.
     * @since 2.0
     */
    public DissolveTransition(final int batches) {
        this.item = ItemStackBuilder.of(Material.STAINED_GLASS_PANE)
            .durability((short) 15)
            .name("&a")
            .build();
        this.batches = batches;
    }

    /**
     * Creates a dissolve transition with the item as black stained glass
     * with no name and dissolve in batches of 1.
     *
     * @since 2.0
     */
    public DissolveTransition() {
        this(1);
    }

    private void dissolveOut(final @NotNull HumanEntity entity, final @NotNull GuiModel dummy, final @NotNull GuiModel destination) {
        // Generate a random pattern to dissolve.
        final List<Integer> slots = IntStream.range(0, dummy.getInventory().getSize())
            .boxed()
            .collect(Collectors.toList());
        Collections.shuffle(slots);
        final int size = slots.size();

        final BukkitTask task = Tasks.scheduleTimer(PluginAdapter.getPlugin(), 2, 1, runnable -> {
            for(int i = 0; i < batches; i++) {
                if(slots.size() == 0) {
                    dummy.updateAll();
                    dissolveIn(entity, dummy, destination);
                    runnable.cancel();
                    return;
                }

                final int slot = slots.remove(0);
                final GuiComponent<?> component = new StaticComponent(destination.getInventory().getItem(slot));
                dummy.mount(slot, new TemporaryComponent(item, size / batches, component));
            }
            dummy.updateAll();
        });
        dummy.onClose(task::cancel);
    }

    private void dissolveIn(final @NotNull HumanEntity entity, final @NotNull GuiModel dummy, final @NotNull GuiModel destination) {
        Tasks.scheduleLater(PluginAdapter.getPlugin(), dummy.getInventory().getSize() / batches, runnable -> {
            if(entity.getOpenInventory().getTopInventory().getHolder() != dummy) return;
            destination.open(entity);
        });
    }

    @Override
    public void startTransition(final @NotNull HumanEntity entity, final @NotNull GuiModel source, final @NotNull GuiModel destination) {
        checkSource(source);
        if(source.getInventory().getSize() != destination.getInventory().getSize())
            throw new IllegalArgumentException("Inventories during transitions must be of same size.");

        final GuiModel dummy = generateCopy(source);
        destination.updateAll();
        dummy.open(entity);

        // Dissolve out.
        dissolveOut(entity, dummy, destination);
    }

}
