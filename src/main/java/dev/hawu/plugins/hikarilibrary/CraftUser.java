package dev.hawu.plugins.hikarilibrary;

import dev.hawu.plugins.api.i18n.Locale;
import dev.hawu.plugins.api.user.ExtendedUser;
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
    public @NotNull Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(final @NotNull Locale locale) {
        this.locale = locale;
    }

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("uuid", uuid.toString());
        map.put("locale", locale.name());
        return map;
    }

}
