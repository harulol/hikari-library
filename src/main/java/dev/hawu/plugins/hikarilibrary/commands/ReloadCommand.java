package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.commands.*;
import dev.hawu.plugins.hikarilibrary.HikariLibrary;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ReloadCommand implements CommandRegistrable {

    @Command("hikarilibrary reload")
    @CommandPermission("hikari-library.reload")
    public void onReloadCommand(final CommandSource sender, final CommandArgument args) {
        final Map<String, List<String>> cli = new CommandLine().withFlags("-f").parse(args).getSecond();
        if(cli.containsKey("-f")) {
            HikariLibrary.getModule().saveResources(true);
            sender.translate(HikariLibrary.getModule(), "overwritten-messages");
        } else {
            HikariLibrary.getModule().saveResources(false);
            sender.translate(HikariLibrary.getModule(), "reloaded-messages");
        }
    }

    @TabExecute("hikarilibrary reload")
    @CommandPermission("hikari-library.reload")
    public List<String> onReloadTab(final CommandSource sender, final CommandArgument args) {
        if(args.size() == 1 && !args.getUnderlyingList().contains("-f")) return Collections.singletonList("-f");
        else return null;
    }

}
