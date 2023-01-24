package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.commands.*;
import dev.hawu.plugins.api.gui.GuiModel;
import dev.hawu.plugins.api.gui.brushes.BrushRegistry;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.Material;

/**
 * The command handler for testing the library's features.
 *
 * @since 2.0
 */
public final class TestCommand implements CommandRegistrable {

    @Command(value = "hikarilibrary test", senders = {SenderType.PLAYER})
    @CommandPermission("hikari-library.test")
    public void run(final CommandSource sender, final CommandArgument args) {
        final GuiModel model = new GuiModel(54, "Test Inventory");
        BrushRegistry.border()
            .model(model)
            .addComponent(() -> ItemStackBuilder.of(Material.STAINED_GLASS_PANE)
                .name("&a")
                .durability((short) 5)
                .toStaticComponent())
            .addComponent(() -> ItemStackBuilder.of(Material.STAINED_GLASS_PANE)
                .name("&a")
                .durability((short) 4)
                .toStaticComponent())
            .addComponent(() -> ItemStackBuilder.of(Material.STAINED_GLASS_PANE)
                .name("&a")
                .durability((short) 6)
                .toStaticComponent())
            .addComponent(() -> ItemStackBuilder.of(Material.STAINED_GLASS_PANE)
                .name("&a")
                .durability((short) 9)
                .toStaticComponent())
            .build()
            .apply();

        sender.getPlayer().ifPresent(model::open);
    }

}
