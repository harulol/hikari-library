package dev.hawu.plugins.api.v1_8_R1;

import dev.hawu.plugins.api.particles.ParticleEffect;
import dev.hawu.plugins.api.adapters.ParticlePacketAdapter;
import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
public class SimpleParticlePacketAdapter extends ParticlePacketAdapter {

    @Override
    public void send(final @NotNull Player player, final @NotNull ParticleEffect effect) {
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
            EnumParticle.valueOf(effect.effect.name()),
            effect.longDistance,
            (float) effect.x, (float) effect.y, (float) effect.z,
            effect.offsetX, effect.offsetY, effect.offsetZ,
            effect.particleData, effect.particleCount,
            effect.data
        );
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
