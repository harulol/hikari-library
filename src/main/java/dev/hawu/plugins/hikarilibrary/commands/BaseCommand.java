package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.commands.*;
import dev.hawu.plugins.api.reflect.ClassVisitor;
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
        ClassVisitor.scavenge();
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
