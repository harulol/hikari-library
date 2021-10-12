package dev.hawu.plugins.api.v1_8_R2;

import dev.hawu.plugins.api.particles.ParticleEffect;
import dev.hawu.plugins.api.particles.ParticlePacketAdapter;
import net.minecraft.server.v1_8_R2.EnumParticle;
import net.minecraft.server.v1_8_R2.Packet;
import net.minecraft.server.v1_8_R2.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Packet adapter for version {@code v1_8_R2}.
 *
 * @since 1.0
 * @deprecated Use {@link ParticlePacketAdapter#getAdapter()} instead.
 */
@SuppressWarnings("DuplicatedCode")
@Deprecated
public final class SimpleParticlePacketAdapter extends ParticlePacketAdapter {

    @Override
    public void send(final @NotNull Player player, final @NotNull ParticleEffect effect) {
        final EnumParticle enumParticle = EnumParticle.valueOf(effect.particle.toUpperCase().replace(' ', '_'));
        final Packet<?> packet = new PacketPlayOutWorldParticles(enumParticle, effect.longDistance, (float) effect.x, (float) effect.y, (float) effect.z,
            effect.offsetX, effect.offsetY, effect.offsetZ, effect.particleData, effect.particleCount, effect.data);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
