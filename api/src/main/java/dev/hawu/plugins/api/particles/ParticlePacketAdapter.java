package dev.hawu.plugins.api.particles;

import dev.hawu.plugins.api.reflect.LookupException;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import org.bukkit.entity.Player;
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
        if(adapter != null) return adapter;

        try {
            return adapter = (ParticlePacketAdapter) Class.forName(computeClassURL()).newInstance();
        } catch(final Exception exception) {
            throw new LookupException(exception);
        }
    }

    @NotNull
    private static String computeClassURL() {
        return "dev.hawu.plugins.api." + getFallbackVersionTag() + ".SimpleParticlePacketAdapter";
    }

    @NotNull
    private static String getFallbackVersionTag() {
        switch(MinecraftVersion.getCurrent()) {
            case v1_8_R1:
            case v1_8_R2:
            case v1_8_R3:
                return MinecraftVersion.getCurrent().name();
            default:
                return MinecraftVersion.v1_9_R1.name();
        }
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
