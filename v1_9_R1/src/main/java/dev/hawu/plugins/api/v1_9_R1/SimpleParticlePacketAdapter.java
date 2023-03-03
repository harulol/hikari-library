package dev.hawu.plugins.api.v1_9_R1;

import dev.hawu.plugins.api.adapters.ParticlePacketAdapter;
import dev.hawu.plugins.api.particles.ParticleEffect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
public class SimpleParticlePacketAdapter extends ParticlePacketAdapter {

    @Override
    public void send(final @NotNull Player player, final @NotNull ParticleEffect effect) {
        player.spawnParticle(Particle.valueOf(effect.effect.name()),
                effect.x, effect.y, effect.z, effect.particleCount, effect.offsetX, effect.offsetY, effect.offsetZ, effect.data);
    }

}
