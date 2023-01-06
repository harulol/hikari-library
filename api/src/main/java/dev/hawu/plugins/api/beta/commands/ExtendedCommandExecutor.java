package dev.hawu.plugins.api.beta.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * The extended executor that provides an additional hash map
 * for child commands in each instance.
 *
 * @since 2.0
 */
public abstract class ExtendedCommandExecutor implements CommandExecutor {

    public final Map<String, CommandExecutor> subCommands = new HashMap<>();

    @Override
    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

}
