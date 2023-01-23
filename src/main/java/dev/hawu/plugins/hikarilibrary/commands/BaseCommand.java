package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.commands.*;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

import static dev.hawu.plugins.hikarilibrary.HikariLibrary.tl;

/**
 * The base command of the plugin.
 */
public final class BaseCommand implements CommandRegistrable {

    private final JavaPlugin plugin;
    private final List<String> subCommands = Collections.singletonList("locale");

    public BaseCommand(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("test")
    public void testCommand(final CommandSource sender, final CommandArgument args) {
        final ItemStack item = ItemStackBuilder.of(Material.DIAMOND)
            .name("&e&lDiamond! %hehe% %haha%")
            .lore("&7Diamonds...", "&7%hehe%", "&7%haha%")
            .replaceText(Pair.of("haha", "Hello Mom!"))
            .build();
        final ItemStack clone = ItemStackBuilder.from(item)
            .replaceText(Pair.of("hehe", "Hello World!"))
            .build();
        sender.getPlayer().get().getInventory().addItem(clone);
    }

    @Command("hikarilibrary")
    @CommandPermission("hikari-library.base")
    public void runBase(final CommandSource sender, final CommandArgument args) {
        sender.sendMessage(tl(sender.getLocale(), "current-version", Pair.of("version", plugin.getDescription().getVersion())));
    }

    @TabExecute("hikarilibrary")
    @CommandPermission("hikari-library.base")
    public List<String> baseTab(final CommandSource sender, final CommandArgument args) {
        if(args.size() == 1) return subCommands;
        else return null;
    }

}
