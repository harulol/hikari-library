package dev.hawu.plugins.api.commands;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class BaseCommand extends AbstractCommandClass {

    private final JavaPlugin plugin;

    public BaseCommand(final @NotNull JavaPlugin plugin) {
        super("hpm");
        this.plugin = plugin;
        this.setPermission("hpm.main");
        register(plugin);
        allowAny();
    }

    @Nullable
    public static String extractString(final @NotNull Map<String, List<String>> properties, final @NotNull String @NotNull ... args) {
        for(final String s : args) {
            if(properties.containsKey(s) && properties.get(s).size() >= 1) {
                return properties.get(s).get(0);
            }
        }
        return null;
    }

    @NotNull
    public static String extractString(final @NotNull String ifNotExist,
                                       final @NotNull Map<String, List<String>> properties, final @NotNull String @NotNull ... args) {
        final String result = extractString(properties, args);
        return result == null ? ifNotExist : result;
    }

    public static int extractInt(final int ifError, final @NotNull Map<String, List<String>> properties, final @NotNull String @NotNull ... args) {
        try {
            return Integer.parseInt(extractString("-1", properties, args));
        } catch(final NumberFormatException exception) {
            return ifError;
        }
    }

    @Override
    public @NotNull List<String> tab(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        return null;
    }

    @Override
    public void run(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        sender.sendMessage("&cFeature disabled for rewriting.");
    }

}
