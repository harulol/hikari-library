package dev.hawu.plugins.api.misc;

import dev.hawu.plugins.api.collections.CooldownMap;
import dev.hawu.plugins.api.collections.Property;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * A messenger that attempts to send messages to players
 * at a certain rate.
 * <p>
 * For example, when they suffocate, and you want to send a notice,
 * but sending every 0.5s they take damage would be too much.
 *
 * @since 1.6
 */
public final class SporadicMessenger {

    private final CooldownMap<UUID> map;
    private final String message;

    /**
     * Creates the messenger with a cooldown of the given amount of milliseconds.
     *
     * @param cooldown the cooldown in milliseconds
     * @since 1.6
     */
    public SporadicMessenger(final Number cooldown, final @NotNull String message) {
        map = new CooldownMap<>(cooldown.longValue());
        this.message = message;
    }

    /**
     * Creates the messenger with 3s cooldown.
     *
     * @since 1.6
     */
    public SporadicMessenger(final @NotNull String message) {
        this(3000, message);
    }

    /**
     * Attempts to send the player a message.
     *
     * @param uuid the player's UUID
     * @return true if the player was not on cooldown, false otherwise
     * @since 1.6
     */
    public boolean send(final UUID uuid) {
        if(map.isOnCooldown(uuid)) return false;
        map.putOnCooldown(uuid);
        Property.of(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(message));
        return true;
    }

    /**
     * Attempts to send the player a message.
     *
     * @param player the player
     * @return true if the player was not on cooldown, false otherwise
     * @since 1.6
     */
    public boolean send(final @NotNull Player player) {
        if(map.isOnCooldown(player.getUniqueId())) return false;
        map.putOnCooldown(player.getUniqueId());
        player.sendMessage(message);
        return true;
    }

}
