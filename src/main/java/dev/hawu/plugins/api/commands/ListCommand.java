package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.PackagesManager;
import dev.hawu.plugins.api.collections.tuples.Pair;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@SuppressWarnings("DuplicatedCode")
public final class ListCommand extends AbstractCommandClass {

    private final CommandLine cli = new CommandLine().withArgument("-q").withArgument("--query")
        .withArgument("-n").withArgument("--count").withFlag("-e").withFlag("--exact").withFlag("--id").withFlag("--name");

    private final String syntax;

    ListCommand(final @NotNull String syntax) {
        super("list");
        this.syntax = syntax;

        setPermission("hpm.list");
        allowAny();
    }

    @Override
    public @NotNull List<String> tab(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        return null;
    }

    @Override
    public void run(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        final Pair<CommandArgument, Map<String, List<String>>> pair = args.withCLI(cli).parse();
        final CommandArgument arguments = pair.getFirst();
        final Map<String, List<String>> properties = pair.getSecond();

        assert arguments != null;
        assert properties != null;

        if(properties.containsKey("-?") || properties.containsKey("--help")) {
            sender.sendMessage(syntax);
            return;
        }

        final String query = BaseCommand.extractString(arguments.joinToString(0, arguments.size()), properties, "-q", "--query");
        final int count = BaseCommand.extractInt(-1, properties, "-n", "--count");

        // Make sure all packages are cached before continuing
        PackagesManager.getInstance().cache(sender.getBase(), () -> PackagesManager.getInstance().filterPackages(query,
                properties.containsKey("-e") || properties.containsKey("--exact"),
                properties.containsKey("--id"), properties.containsKey("--name"), count)
            .filter(pkg -> PackagesManager.getInstance().isPackageInstalled(pkg.getId()))
            .forEach(pkg -> {
                final Plugin pl = Bukkit.getPluginManager().getPlugin(pkg.getName());
                final StringBuilder msg = new StringBuilder();

                msg.append("&7* ").append(pkg.getName()).append(" &8(").append(pkg.getId()).append(") ")
                    .append("&7v").append(pl.getDescription().getVersion());
                if(!pkg.isCompatible()) msg.append(" &c&lIncompatible");
                if(pkg.isPremium()) msg.append(" &6&lPremium");
                if(!pl.isEnabled()) msg.append(" &4&lDisabled");
                if(pkg.getLatestRelease() != null && pkg.getLatestRelease() != pkg.getLatestRelease())
                    msg.append(" &7(&av").append(pkg.getLatestRelease()).append("&7)");

                sender.sendMessage(msg.toString());
            }), false);
    }

}
