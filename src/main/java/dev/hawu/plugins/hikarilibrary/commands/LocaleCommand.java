package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.commands.*;
import dev.hawu.plugins.api.i18n.Locale;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static dev.hawu.plugins.hikarilibrary.HikariLibrary.tl;

/**
 * The command to change the locale of the player. Messages towards
 * this player should be tailored according to this setting.
 */
public final class LocaleCommand implements CommandRegistrable {

    @Command(value = "hikarilibrary locale", senders = {SenderType.PLAYER})
    @CommandPermission("hikari-library.locale")
    public void run(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        if(args.size() == 0) {
            sender.sendMessage(tl(sender.getLocale(), "current-locale", Pair.of("locale", sender.getLocale().getDisplayName())));
            return;
        }

        final String localeString = args.get(0);
        try {
            final Locale locale = Locale.valueOf(localeString);
            sender.getUser().ifPresent(user -> user.setLocale(locale));
            sender.sendMessage(tl(sender.getLocale(), "change-locale", Pair.of("locale", locale.getDisplayName())));
        } catch(final IllegalArgumentException ex) {
            sender.sendMessage(tl(sender.getLocale(), "unknown-locale", Pair.of("locale", localeString)));
        }
    }

    @TabExecute(value = "hikarilibrary locale", senders = SenderType.PLAYER)
    @CommandPermission("hikari-library.locale")
    public List<String> tab(final @NotNull CommandSource sender, final CommandArgument args) {
        if(args.size() == 1) return Locale.LOCALES;
        else return null;
    }

}
