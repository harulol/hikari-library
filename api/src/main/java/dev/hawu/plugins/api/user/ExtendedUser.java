package dev.hawu.plugins.api.user;

import dev.hawu.plugins.api.i18n.Locale;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
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

}
