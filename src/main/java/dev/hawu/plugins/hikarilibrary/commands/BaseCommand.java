package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.commands.AbstractCommandClass;
import dev.hawu.plugins.api.commands.CommandArgument;
import dev.hawu.plugins.api.commands.CommandSource;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * The base command of the plugin.
 */
public final class BaseCommand extends AbstractCommandClass {

    public BaseCommand(final JavaPlugin plugin) {
        super("hikarilibrary");
        setPermission("hikari-library.base");
        allowPlayers();

        bind(new LocaleCommand());

        register(plugin);
    }

    @Override
    public void run(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        sender.sendMessage("&cUsage: /hikari <args...>");
    }

    @Override
    public @NotNull List<String> tab(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        if(args.size() == 1) return Collections.singletonList("locale");
        else return Collections.emptyList();
    }

}
