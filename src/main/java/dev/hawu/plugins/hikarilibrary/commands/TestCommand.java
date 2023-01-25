package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.commands.*;
import dev.hawu.plugins.api.gui.GuiModel;
import dev.hawu.plugins.api.gui.templates.AnimatedComponent;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class TestCommand implements CommandRegistrable {

    private GuiModel getModel() {
        final GuiModel model = new GuiModel(9, "Test Single");
        model.mount(4, new AnimatedComponent(ItemStackBuilder.of(Material.DIAMOND).build()) {
            final String NAME = "A very shiny diamond";
            long wait = 0;

            @Override
            public @NotNull String getDisplayName() {
                if(wait >= this.state) return "&3" + NAME;

                int index = this.state % NAME.length();
                if(index == NAME.length() - 1) wait = this.state + 20;
                return "&3" + NAME.substring(0, index) +
                    "&b&l" + NAME.substring(index, Math.min(index + 2, NAME.length())) +
                    "&3" + NAME.substring(Math.min(NAME.length(), index + 2));
            }

            @Override
            public @NotNull List<String> getLore() {
                final String seconds = Strings.format(this.state / 20);
                return Collections.singletonList("&c" + seconds + "s");
            }
        });
        return model;
    }

    @Command("hikarilibrary test")
    @CommandPermission("hikari-library.test")
    public void runTest(final CommandSource sender, final CommandArgument args) {
        sender.getPlayer().ifPresent(getModel()::open);
    }

}
