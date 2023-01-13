package dev.hawu.plugins.api.adapters;

import dev.hawu.plugins.api.exceptions.LookupException;
import dev.hawu.plugins.api.particles.ParticleEffect;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * The cross-version adapter for spawning world particles
 * to players.
 *
 * @since 1.0
 */
public abstract class ParticlePacketAdapter {

    private static ParticlePacketAdapter adapter;

    /**
     * Retrieves an instance of the particle packet adapter, and computes
     * one if it isn't yet.
     *
     * @return A non-null instance of a particle adapter.
     * @since 1.0
     */
    @NotNull
    public static ParticlePacketAdapter getAdapter() {
        try {
            if(adapter == null) {
                final String version = MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_9_R1) ? "v1_9_R1" : MinecraftVersion.getCurrent().name();
                return adapter = (ParticlePacketAdapter) Class.forName("dev.hawu.plugins.api." + version + ".SimpleParticlePacketAdapter")
                    .getConstructor().newInstance();
            } else return adapter;
        } catch(final Exception exception) {
            throw new LookupException(exception);
        }
    }

    /**
     * Sets the adapter for particle packets.
     *
     * @param packetAdapter The packet adapter implementation.
     * @since 1.3
     */
    @Internal
    public static void setAdapter(final @NotNull ParticlePacketAdapter packetAdapter) {
        if(adapter == null) adapter = packetAdapter;
    }

    /**
     * Sends a particle effect to a player.
     *
     * @param player The player whom to send to.
     * @param effect The effect to send.
     * @since 1.0
     */
    public abstract void send(final @NotNull Player player, final @NotNull ParticleEffect effect);

}
