package dev.hawu.plugins.api.chat;

import dev.hawu.plugins.api.exceptions.AlreadyInitializedException;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The adapter for sending customized
 * chat packets.
 *
 * @since 1.5
 */
public abstract class ChatPacketAdapter {

    private static ChatPacketAdapter adapter;

    /**
     * Sends a text component to the player.
     *
     * @param player    The player to send to.
     * @param component The component to send.
     * @since 1.5
     */
    public abstract void sendPlayer(final @NotNull Player player, final @NotNull TextComponent component);

    /**
     * Sends a text component to all online players.
     *
     * @param component The component to send.
     * @since 1.5
     */
    public abstract void sendAll(final @NotNull TextComponent component);

    /**
     * Sends a wrapped message to the player.
     *
     * @param player  The player to send to.
     * @param message The message to send.
     * @since 1.5
     */
    public abstract void sendPlayer(final @NotNull Player player, final @NotNull String message);

    /**
     * Sends a wrapped message to all online players.
     *
     * @param message The message to send.
     * @since 1.5
     */
    public abstract void sendAll(final @NotNull String message);

    /**
     * Retrieves the set adapter instance.
     *
     * @return The adapter instance.
     * @since 1.5
     */
    @NotNull
    public static ChatPacketAdapter getAdapter() {
        return Objects.requireNonNull(adapter, "ChatPacketAdapter is not yet initialized.");
    }

    /**
     * Sets the adapter instance for chat packets.
     *
     * @param packetAdapter The adapter instance.
     * @since 1.5
     */
    public static void setAdapter(final @NotNull ChatPacketAdapter packetAdapter) {
        if(adapter != null) throw new AlreadyInitializedException("ChatPacketAdapter is already initialized.");
        adapter = packetAdapter;
    }

    /**
     * Attempts to resolve for an implementation of the
     * chat packet adapter based on the server's current version.
     *
     * Usually, these kinds of implementations are repeated
     * and they might be replaced with just one class using
     * static final method handles soon.
     *
     * @since 1.5
     */
    @Deprecated
    public static void resolve() {
        if(adapter != null) return;

        try {
            adapter = (ChatPacketAdapter) Class.forName("dev.hawu.plugins.api." + MinecraftVersion.getCurrent() + ".SimpleChatRegistry").newInstance();
        } catch(final Exception e) {
            e.printStackTrace();
        }
    }

}
