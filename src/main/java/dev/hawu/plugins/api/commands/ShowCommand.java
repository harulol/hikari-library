package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.PackagesManager;
import dev.hawu.plugins.api.PluginPackage;
import dev.hawu.plugins.api.collections.tuples.Pair;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
public final class ShowCommand extends AbstractCommandClass {

    private final CommandLine cli = new CommandLine().withArgument("-q").withArgument("--query")
        .withFlag("-e").withFlag("--exact").withFlag("--id").withFlag("--name").withFlag("-p").withFlag("--pre").withFlag("--dev");
    private final String syntax;

    ShowCommand(final @NotNull String syntax) {
        super("show");
        this.syntax = syntax;

        allowAny();
        setPermission("hpm.show");
    }

    @Override
    public @Nullable List<String> tab(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
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

        // Make sure all packages are cached before continuing
        PackagesManager.getInstance().cache(sender, () -> {
            final List<PluginPackage> packages = PackagesManager.getInstance().filterPackages(query,
                properties.containsKey("-e") || properties.containsKey("--exact"),
                properties.containsKey("--id"), properties.containsKey("--name"), -1).collect(Collectors.toList());

            if(packages.size() == 0) {
                PackagesManager.warnIfNotNull(sender, "There are no packages matching the specified parameters.");
                return;
            } else if(packages.size() > 1) {
                PackagesManager.warnIfNotNull(sender, "There are too many packages matching the specified parameters. Please refine the inputs.");
                packages.forEach(pkg -> sender.sendMessage(String.format("- %s (%s)", pkg.getName(), pkg.getId())));
                return;
            }

            final PluginPackage pkg = packages.get(0);
            final StringBuilder sb = new StringBuilder();

            sb.append("&7Found &b").append(pkg.getName()).append(" &8(").append(pkg.getId()).append(")\n");

            sb.append("&7  * Status: ");
            if(PackagesManager.getInstance().isPackageInstalled(pkg.getId())) {
                if(Bukkit.getPluginManager().getPlugin(pkg.getName()).isEnabled()) sb.append("&aEnabled");
                else sb.append("&cDisabled");
            } else sb.append("&cNot Installed");
            sb.append("\n");

            sb.append("&7  * Current Version: ");
            if(PackagesManager.getInstance().isPackageInstalled(pkg.getId())) {
                final Plugin pl = Bukkit.getPluginManager().getPlugin(pkg.getName());
                if(pl != null) sb.append("&ev").append(pl.getDescription().getVersion()).append("\n");
                else sb.append("&cN/A").append("\n");
            } else sb.append("&cN/A").append("\n");

            sb.append("&7  * Premium: ").append(pkg.isPremium() ? "&atrue" : "&cfalse").append("\n");
            sb.append("&7  * Description: ").append(pkg.getDescription()).append("\n");
            sb.append("&7  * Compatible: ").append(pkg.isCompatible() ? "&atrue" : "&cfalse").append("\n");
            if(pkg.getDependencies().size() > 0) sb.append("&7  * Dependencies: ").append(String.join(", ", pkg.getDependencies())).append("\n");
            sb.append("&7  * Latest Release: ").append(pkg.getLatestRelease() == null ? "&cN/A" : "&a" + pkg.getLatestRelease().getTagName()).append("\n");

            if(properties.containsKey("-p") || properties.containsKey("--pre")) {
                sb.append("&7  * Latest Prerelease: ").append(pkg.getLatestPrerelease() == null ? "&cN/A" : "&a" + pkg.getLatestPrerelease().getTagName()).append("\n");
            }
            if(properties.containsKey("--dev"))
                sb.append("&7  * Latest Dev: ").append(pkg.getLatestDev() == null ? "&cN/A" : "&a" + pkg.getLatestDev().getTagName()).append("\n");

            sender.sendMessage(sb.toString());
        }, false);
    }

}
