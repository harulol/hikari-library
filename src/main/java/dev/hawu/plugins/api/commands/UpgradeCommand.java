package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.PackagesManager;
import dev.hawu.plugins.api.PluginPackage;
import dev.hawu.plugins.api.collections.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
public final class UpgradeCommand extends AbstractCommandClass {

    private final CommandLine cli = new CommandLine().withArgument("-q").withArgument("--query")
        .withFlag("-e").withFlag("--exact").withFlag("--id").withFlag("--name").withFlag("--force").withFlag("--nogc").withFlag("-r").withFlag("--verbose");
    private final String syntax;

    UpgradeCommand(final @NotNull String syntax) {
        super("upgrade");
        this.syntax = syntax;

        setPermission("hpm.upgrade");
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
            String version = BaseCommand.extractString(properties, "-v", "--version");
            if(pkg.getLatestRelease() != null && version == null) version = pkg.getLatestRelease().getTagName();

            if(version == null) {
                PackagesManager.severeIfNotNull(sender, "Couldn't find any version to install.");
                return;
            }

            PackagesManager.getInstance().upgradePackage(sender, packages.get(0), version,
                properties.containsKey("--verbose"), properties.containsKey("--force"));
        }, true);
    }

}
