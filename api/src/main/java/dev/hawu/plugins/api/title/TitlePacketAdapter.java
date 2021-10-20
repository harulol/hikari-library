package dev.hawu.plugins.api.title;

import dev.hawu.plugins.api.reflect.LookupException;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A base abstracted packet adapter for sending and
 * clearing titles across versions.
 *
 * @since 1.0
 */
public abstract class TitlePacketAdapter {

    private static TitlePacketAdapter adapter;

    /**
     * Retrieves the adapter instance for sending and clearing titles,
     * or computes that instance if it doesn't already exist.
     *
     * @return The adapter instance.
     * @since 1.0
     */
    @NotNull
    public static TitlePacketAdapter getAdapter() {
        if(adapter != null) return adapter;

        try {
            return adapter =
                (TitlePacketAdapter) Class.forName("dev.hawu.plugins.api." + MinecraftVersion.getCurrent().name() + ".SimpleTitlePacketAdapter").newInstance();
        } catch(final Exception exception) {
            throw new LookupException(exception);
        }
    }

    /**
     * Sends a title component to a player.
     *
     * @param player    The player to send to.
     * @param component The component to send.
     * @since 1.0
     */
    public abstract void send(final @NotNull Player player, final @NotNull TitleComponent component);

    /**
     * Sends a title component to a player that needs to be displayed
     * in the action bar slot.
     *
     * @param player    The player to send to.
     * @param component The component to send.
     * @since 1.0
     */
    public abstract void sendActionBar(final @NotNull Player player, final @NotNull TitleComponent component);

    /**
     * Attempts to clear an existing player's titles.
     *
     * @param player The player whose title to clear.
     * @since 1.0
     */
    public abstract void clear(final @NotNull Player player);

}
