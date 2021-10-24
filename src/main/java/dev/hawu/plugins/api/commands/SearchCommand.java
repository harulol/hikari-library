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
public final class SearchCommand extends AbstractCommandClass {

    private final CommandLine cli = new CommandLine().withArgument("-q").withArgument("--query")
        .withFlag("-e").withFlag("--exact").withFlag("--id").withFlag("--name").withArgument("-n").withArgument("--count");
    private final String syntax;

    SearchCommand(final @NotNull String syntax) {
        super("search");
        this.syntax = syntax;

        allowAny();
        setPermission("hpm.search");
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
        final int count = BaseCommand.extractInt(-1, properties, "-n", "--count");

        // Make sure all packages are cached before continuing
        PackagesManager.getInstance().cache(sender.getBase(), () -> {
            final List<PluginPackage> packages = PackagesManager.getInstance().filterPackages(query,
                properties.containsKey("-e") || properties.containsKey("--exact"),
                properties.containsKey("--id"), properties.containsKey("--name"), count).collect(Collectors.toList());
            if(packages.isEmpty()) {
                PackagesManager.warnIfNotNull(sender.getBase(), "There are no packages matching the specified parameters.");
                return;
            }

            packages.forEach(pkg -> sender.sendMessage(String.format("&7Found &b%s %s &8(%s)", pkg.getName(), pkg.getLatestRelease() != null
                ? "v" + pkg.getLatestRelease().getTagName() : "(unreleased)", pkg.getId())));
        }, false);
    }

}
