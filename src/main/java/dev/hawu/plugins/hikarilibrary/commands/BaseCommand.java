package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.commands.*;
import dev.hawu.plugins.hikarilibrary.HikariLibrary;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * The base command of the plugin.
 */
public final class BaseCommand implements CommandRegistrable {

    private final JavaPlugin plugin;
    private final List<String> subCommands = Arrays.asList("locale", "test", "reload");

    public BaseCommand(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("hikarilibrary")
    @CommandPermission("hikari-library.base")
    public void runBase(final CommandSource sender, final CommandArgument args) {
        sender.translate(HikariLibrary.getModule(), "current-version",
            Pair.of("version", plugin.getDescription().getVersion()));
    }

    @TabExecute("hikarilibrary")
    @CommandPermission("hikari-library.base")
    public List<String> baseTab(final CommandSource sender, final CommandArgument args) {
        if(args.size() == 1) return subCommands;
        else return null;
    }

}
