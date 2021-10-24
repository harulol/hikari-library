package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.PackagesManager;
import dev.hawu.plugins.api.PluginPackage;
import dev.hawu.plugins.api.collections.tuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
public final class InstallCommand extends AbstractCommandClass {

    private final CommandLine cli = new CommandLine().withArgument("-q").withArgument("--query")
        .withFlag("-e").withFlag("--exact").withFlag("--id").withFlag("--name").withArgument("-v").withArgument("--ver")
        .withFlag("-l").withFlag("--load").withFlag("--force");
    private final String syntax;

    InstallCommand(final @NotNull String syntax) {
        super("install");
        this.syntax = syntax;

        allowAny();
        setPermission("hpm.install");
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
        PackagesManager.getInstance().cache(sender.getBase(), () -> {
            final List<PluginPackage> packages = PackagesManager.getInstance().filterPackages(query,
                properties.containsKey("-e") || properties.containsKey("--exact"),
                properties.containsKey("--id"), properties.containsKey("--name"), -1).collect(Collectors.toList());

            if(packages.size() == 0) {
                PackagesManager.warnIfNotNull(sender.getBase(), "There are no packages matching the specified parameters.");
                return;
            } else if(packages.size() > 1) {
                PackagesManager.warnIfNotNull(sender.getBase(), "There are too many packages matching the specified parameters. Please refine the inputs.");
                packages.forEach(pkg -> sender.sendMessage(String.format("- %s (%s)", pkg.getName(), pkg.getId())));
                return;
            }

            final PluginPackage pkg = packages.get(0);
            String version = BaseCommand.extractString(properties, "-v", "--ver");
            version = version == null && pkg.getLatestRelease() != null ? pkg.getLatestRelease().getTagName() : version;

            if(version == null) {
                PackagesManager.severeIfNotNull(sender.getBase(), "Unable to fetch version to install.");
                return;
            }

            PackagesManager.getInstance().installPackage(sender.getBase(), pkg, version, properties.containsKey("--verbose"),
                properties.containsKey("-l") || properties.containsKey("--load"), properties.containsKey("--force"));
        }, true);
    }

}
