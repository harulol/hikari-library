package dev.hawu.plugins.api.user;

import dev.hawu.plugins.api.collections.Property;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * The adapter for extended functions on a Player.
 *
 * @since 1.6
 */
public abstract class UserAdapter {

    private static UserAdapter adapter;

    /**
     * Retrieves the adapter instance.
     *
     * @return the adapter instance
     * @since 1.6
     */
    @NotNull
    public static UserAdapter getAdapter() {
        return adapter;
    }

    /**
     * Initialize the adapter.
     *
     * @param value the adapter to use
     * @since 1.6
     */
    public static void setAdapter(final @NotNull UserAdapter value) {
        if(adapter != null) throw new IllegalStateException("Adapter already set");
        adapter = value;
    }

    /**
     * Retrieves the user from an uuid.
     *
     * @param uuid the uuid
     * @return the user
     * @since 1.6
     */
    @NotNull
    public abstract ExtendedUser getUser(final @NotNull UUID uuid);

    /**
     * Retrieves the user from a player instance.
     *
     * @param player the player
     * @return the user
     * @since 1.6
     */
    @NotNull
    public abstract ExtendedUser getUser(final @NotNull OfflinePlayer player);

    /**
     * Retrieves the user from an uuid.
     *
     * @param uuid the uuid
     * @return the user
     * @since 1.6
     */
    @NotNull
    public abstract Property<ExtendedUser> getUserOption(final @NotNull UUID uuid);

    /**
     * Retrieves the user from a player instance.
     *
     * @param player the player
     * @return the user
     * @since 1.6
     */
    @NotNull
    public abstract Property<ExtendedUser> getUserOption(final @NotNull OfflinePlayer player);

}
