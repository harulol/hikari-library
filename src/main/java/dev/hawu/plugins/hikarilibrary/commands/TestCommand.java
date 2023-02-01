package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.commands.*;

public final class TestCommand implements CommandRegistrable {

    @Command(value = "hikarilibrary test", senders = SenderType.PLAYER)
    @CommandPermission("hikari-library.test")
    public void runTest(final CommandSource sender, final CommandArgument args) {
    }

}
