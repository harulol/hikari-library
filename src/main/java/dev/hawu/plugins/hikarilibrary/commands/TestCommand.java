package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.commands.*;
import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.gui.GuiModel;
import dev.hawu.plugins.api.gui.pagination.GuiPaginator;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TestCommand implements CommandRegistrable {

    private GuiModel getModel() {
        final GuiModel model = new GuiModel(54, "Test Single");
        model.mount(53, new GuiComponent<Object>() {
            @Override
            public void componentWillUnmount() {
                System.out.println("A component has been unmounted.");
            }

            @Override
            public @NotNull ItemStack render() {
                return ItemStackBuilder.of(Material.DIAMOND)
                    .name("&e&lHello!")
                    .build();
            }
        });

        return model;
    }

    private void openPagination(final Player player) {
        GuiPaginator.<Integer>newBuilder()
            .setCollection(IntStream.range(1, 100).boxed().collect(Collectors.toList()))
            .setItemGenerator((item, it) -> null)
            .setModelSupplier(this::getModel)
            .build(player);
    }

    @Command("hikarilibrary test")
    @CommandPermission("hikari-library.test")
    public void runTest(final CommandSource sender, final CommandArgument args) {
        if(args.size() == 1)
            openPagination(sender.getPlayer().get());
        else getModel().open(sender.getPlayer().get());
    }

}
