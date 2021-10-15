package dev.hawu.plugins.api.v1_9_R1;

import dev.hawu.plugins.api.particles.ParticleEffect;
import dev.hawu.plugins.api.particles.ParticlePacketAdapter;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Packet adapter for version {@code v1_9_R1} and all later versions,
 * since this is already Bukkit's native API, without a need for NMS.
 *
 * @since 1.0
 * @deprecated Use {@link ParticlePacketAdapter#getAdapter()} instead.
 */
@SuppressWarnings("DuplicatedCode")
@Deprecated
public final class SimpleParticlePacketAdapter extends ParticlePacketAdapter {

    @Override
    public void send(final @NotNull Player player, final @NotNull ParticleEffect effect) {
        player.spawnParticle(Particle.valueOf(effect.particle.toUpperCase().replace(' ', '_')),
            effect.x, effect.y, effect.z, effect.particleCount, effect.offsetX, effect.offsetY, effect.offsetZ, effect.particleData, effect.data);
    }

}
