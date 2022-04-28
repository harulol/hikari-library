package dev.hawu.plugins.hikarilibrary.commands;

import dev.hawu.plugins.api.ObjectUtils;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.commands.AbstractCommandClass;
import dev.hawu.plugins.api.commands.CommandArgument;
import dev.hawu.plugins.api.commands.CommandSource;
import dev.hawu.plugins.api.i18n.Locale;
import dev.hawu.plugins.api.user.ExtendedUser;
import dev.hawu.plugins.api.user.UserAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dev.hawu.plugins.hikarilibrary.HikariLibrary.tl;

public final class LocaleCommand extends AbstractCommandClass {

    private final List<String> locales = Arrays.stream(Locale.values()).map(Locale::name).collect(Collectors.toList());

    public LocaleCommand() {
        super("locale");
        setPermission("hikari-library.locale");
        allowPlayers();
    }

    @Override
    public void run(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        final ExtendedUser user = UserAdapter.getAdapter().getUser(sender.getPlayer());
        if(args.size() == 0) {
            sender.sendMessage(tl(user.getLocale(), "current-locale", Pair.of("locale", user.getLocale().getDisplayName())));
            return;
        }

        final String localeString = args.get(0);
        try {
            final Locale locale = Locale.valueOf(localeString);
            user.setLocale(locale);
            sender.sendMessage(tl(user.getLocale(), "change-locale", Pair.of("locale", locale.getDisplayName())));
        } catch(final IllegalArgumentException e) {
            sender.sendMessage(tl(user.getLocale(), "unknown-locale", Pair.of("locale", localeString)));
        }
    }

    @Override
    public @NotNull List<String> tab(final @NotNull CommandSource sender, final @NotNull CommandArgument args) {
        if(args.size() == 1) return locales.stream()
            .filter(l -> l.toLowerCase().startsWith(ObjectUtils.elvis(args.get(args.size() - 1), "").toLowerCase()))
            .collect(Collectors.toList());
        else return Collections.emptyList();
    }

}
