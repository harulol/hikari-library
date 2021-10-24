package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.collections.tuples.Pair;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BaseCommand extends AbstractCommandClass {

    private final JavaPlugin plugin;
    private final String syntax;

    public BaseCommand(final @NotNull JavaPlugin plugin) {
        super("hpm");
        this.plugin = plugin;
        this.setPermission("hpm.main");
        register(plugin);

        final FileConfiguration config = YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(plugin.getResource("syntaxes.yml"))));

        this.syntax = getThenJoinThenReplace(plugin, config, "base");
        bind(new ListCommand(getThenJoinThenReplace(plugin, config, "list")));
        bind(new ShowCommand(getThenJoinThenReplace(plugin, config, "show")));
        bind(new SearchCommand(getThenJoinThenReplace(plugin, config, "search")));
        bind(new InstallCommand(getThenJoinThenReplace(plugin, config, "install")));
        bind(new UninstallCommand(getThenJoinThenReplace(plugin, config, "uninstall")));
        bind(new UpgradeCommand(getThenJoinThenReplace(plugin, config, "upgrade")));

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

    @NotNull
    private static String getThenJoinThenReplace(final @NotNull JavaPlugin plugin, final @NotNull FileConfiguration config, final @NotNull String key) {
        final String value;
        if(config.isList(key)) value = String.join(System.lineSeparator(), config.getStringList(key));
        else value = config.getString(key);
        return Strings.fillPlaceholders(value, new Pair<>("version", plugin.getDescription().getVersion()));
    }

    @Override
    public @NotNull List<String> tab(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        return Stream.of("list", "show", "search", "install", "uninstall", "upgrade")
            .filter(it -> args.get(args.size() - 1) != null && it.startsWith(args.getNonNull(args.size() - 1)))
            .collect(Collectors.toList());
    }

    @Override
    public void run(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        sender.sendMessage(this.syntax);
    }

}
