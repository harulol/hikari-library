package dev.hawu.plugins.api.user;

import dev.hawu.plugins.api.chat.TextComponent;
import dev.hawu.plugins.api.collections.Property;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.i18n.LanguageModule;
import dev.hawu.plugins.api.i18n.Locale;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents an extended {@link org.bukkit.entity.Player}.
 *
 * @since 1.6
 */
public interface ExtendedUser extends ConfigurationSerializable {

    /**
     * Retrieves the UUID of the user.
     *
     * @return the UUID of the user
     * @since 1.6
     */
    @NotNull
    UUID getUUID();

    /**
     * Retrieves the offline player instance of the user.
     *
     * @return the offline player instance
     * @since 2.0
     */
    @NotNull
    OfflinePlayer getOfflinePlayer();

    /**
     * Retrieves the optional player if they are online.
     *
     * @return the player option
     * @since 2.0
     */
    @NotNull
    Property<Player> getPlayer();

    /**
     * Retrieves the locale of the user.
     *
     * @return the locale of the user
     * @since 1.6
     */
    @NotNull
    Locale getLocale();

    /**
     * Sets the locale of the user.
     *
     * @param locale the locale to set
     * @since 1.6
     */
    void setLocale(final @NotNull Locale locale);

    /**
     * Sends the player a message based on the provided language module.
     *
     * @param module       the module
     * @param key          the key to translate
     * @param replacements the replacements
     */
    void sendMessage(final @NotNull LanguageModule module, final @NotNull String key, final @NotNull Pair<?, ?>... replacements);

    /**
     * Sends a simple colorized message to the user.
     *
     * @param message the message
     * @since 2.0
     */
    void sendMessage(final @NotNull String message);

    /**
     * Sends the user a text component if they are online.
     *
     * @param component the component
     * @since 2.0
     */
    void sendComponent(final @NotNull TextComponent component);

    /**
     * Sends the player a colorized title.
     *
     * @param title    the title
     * @param subtitle the subtitle
     * @param fadeIn   the ticks to fade in
     * @param stay     the ticks to stay
     * @param fadeOut  the ticks to fade out
     * @since 2.0
     */
    void sendTitle(final @NotNull String title, final @NotNull String subtitle, final long fadeIn, final long stay, final long fadeOut);

    /**
     * Sends the player a translated title.
     *
     * @param module       the module
     * @param titleKey     the key to translate the title
     * @param subtitleKey  the key to translate the subtitle
     * @param fadeIn       the ticks to fade in
     * @param stay         the ticks to stay
     * @param fadeOut      the ticks to fade out
     * @param replacements the replacements
     * @since 2.0
     */
    void sendTitle(final @NotNull LanguageModule module, final @NotNull String titleKey, final @NotNull String subtitleKey,
                   final long fadeIn, final long stay, final long fadeOut, final @NotNull Pair<?, ?>... replacements);

}
