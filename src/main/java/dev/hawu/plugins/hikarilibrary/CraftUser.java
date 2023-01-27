package dev.hawu.plugins.hikarilibrary;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.adapters.ChatPacketAdapter;
import dev.hawu.plugins.api.adapters.TitlePacketAdapter;
import dev.hawu.plugins.api.chat.TextComponent;
import dev.hawu.plugins.api.collections.Property;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.i18n.LanguageModule;
import dev.hawu.plugins.api.i18n.Locale;
import dev.hawu.plugins.api.title.TitleComponent;
import dev.hawu.plugins.api.user.ExtendedUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The user object thing.
 *
 * @since 1.6
 */
public final class CraftUser implements ExtendedUser {

    private final UUID uuid;
    private Locale locale;

    public CraftUser(final @NotNull UUID uuid) {
        this.uuid = uuid;
        this.locale = Locale.en_US;
    }

    @NotNull
    public static CraftUser deserialize(final @NotNull Map<String, Object> map) {
        final UUID uuid = UUID.fromString((String) map.get("uuid"));
        final Locale locale = Locale.valueOf((String) map.get("locale"));

        final CraftUser user = new CraftUser(uuid);
        user.setLocale(locale);
        return user;
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    @Override
    public @NotNull Property<Player> getPlayer() {
        return Property.of(uuid).map(Bukkit::getPlayer);
    }

    @Override
    public @NotNull Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(final @NotNull Locale locale) {
        this.locale = locale;
    }

    @Override
    public void sendMessage(final @NotNull LanguageModule module, final @NotNull String key, final @NotNull Pair<?, ?>... replacements) {
        final String message = module.translate(locale, key, replacements);
        getPlayer().ifPresent(p -> p.sendMessage(message));
    }

    @Override
    public void sendMessage(final @NotNull String message) {
        getPlayer().ifPresent(p -> p.sendMessage(Strings.color(message)));
    }

    @Override
    public void sendComponent(final @NotNull TextComponent component) {
        getPlayer().ifPresent(p -> ChatPacketAdapter.getAdapter().sendPlayer(p, component));
    }

    @Override
    public void sendTitle(final @NotNull String title, final @NotNull String subtitle, final long fadeIn, final long stay, final long fadeOut) {
        final TitleComponent component = TitleComponent.builder()
            .title(Strings.color(title))
            .subtitle(Strings.color(subtitle))
            .timing(fadeIn, stay, fadeOut)
            .build();
        getPlayer().ifPresent(p -> TitlePacketAdapter.getAdapter().send(p, component));
    }

    @Override
    public void sendTitle(final @NotNull LanguageModule module, final @NotNull String titleKey, final @NotNull String subtitleKey,
                          final long fadeIn, final long stay, final long fadeOut, final @NotNull Pair<?, ?>... replacements) {
        final TitleComponent component = TitleComponent.builder()
            .title(module.translate(locale, titleKey, replacements))
            .subtitle(module.translate(locale, subtitleKey, replacements))
            .timing(fadeIn, stay, fadeOut)
            .build();
        getPlayer().ifPresent(p -> TitlePacketAdapter.getAdapter().send(p, component));
    }

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("uuid", uuid.toString());
        map.put("locale", locale.name());
        return map;
    }

}
