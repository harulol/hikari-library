package dev.hawu.plugins.api.beta.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extended version of Bukkit Tab completers that provide
 * a hashmap for each instance.
 *
 * @since 2.0
 */
public abstract class ExtendedTabCompleter implements TabCompleter {

    public final Map<String, TabCompleter> subCompleters = new HashMap<>();

    @Override
    public abstract List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);

}
