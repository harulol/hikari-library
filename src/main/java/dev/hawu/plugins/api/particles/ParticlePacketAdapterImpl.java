package dev.hawu.plugins.api.particles;

import dev.hawu.plugins.api.reflect.MinecraftVersion;
import dev.hawu.plugins.api.reflect.SimpleLookup;
import dev.hawu.plugins.api.reflect.UncheckedHandles;
import dev.hawu.plugins.api.reflect.UncheckedReflects;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Objects;

/**
 * Reflective implementation for sending particle
 * packets to players cross-version.
 *
 * @since 1.3
 */
public final class ParticlePacketAdapterImpl extends ParticlePacketAdapter {

    private static final ParticlePacketAdapterImpl INSTANCE = new ParticlePacketAdapterImpl();

    private static final Class<?> BUKKIT_PARTICLE; // For 1.9+
    private static final Class<?> ENUM_PARTICLE; // For 1.8 - 1.8.8
    private static final Class<?> PACKET_PLAY_OUT_PARTICLES;

    private static final MethodHandle VALUE_OF_ENUM_PARTICLE;
    private static final MethodHandle VALUE_OF_BUKKIT_PARTICLE;
    private static final MethodHandle PACKET_PARTICLES_CONSTRUCTOR;
    private static final MethodHandle SPAWN_PARTICLE;

    static {
        final Lookup lookup = MethodHandles.lookup();

        BUKKIT_PARTICLE = UncheckedReflects.getClass("org.bukkit.Particle").orElse(null);
        ENUM_PARTICLE = SimpleLookup.lookupNMSOrNull("EnumParticle");
        PACKET_PLAY_OUT_PARTICLES = SimpleLookup.lookupNMSOrNull("PacketPlayOutWorldParticles");

        if(BUKKIT_PARTICLE != null) {
            VALUE_OF_BUKKIT_PARTICLE = UncheckedHandles.findStatic(lookup, BUKKIT_PARTICLE, "valueOf",
                    MethodType.methodType(BUKKIT_PARTICLE, String.class))
                .map(handle -> handle.asType(MethodType.methodType(Object.class, String.class)))
                .orElse(null);
            SPAWN_PARTICLE = UncheckedHandles.findVirtual(lookup, Player.class, "spawnParticle",
                    MethodType.methodType(void.class, BUKKIT_PARTICLE, double.class, double.class, double.class, int.class,
                        double.class, double.class, double.class, double.class, Object.class))
                .map(handle -> handle.asType(MethodType.methodType(void.class, Player.class, Object.class,
                    double.class, double.class, double.class, int.class, double.class, double.class, double.class, double.class, int[].class)))
                .orElse(null);
        } else {
            VALUE_OF_BUKKIT_PARTICLE = null;
            SPAWN_PARTICLE = null;
        }

        if(ENUM_PARTICLE != null && PACKET_PLAY_OUT_PARTICLES != null) {
            VALUE_OF_ENUM_PARTICLE = UncheckedHandles.findStatic(lookup, ENUM_PARTICLE, "valueOf",
                    MethodType.methodType(ENUM_PARTICLE, String.class))
                .map(handle -> handle.asType(MethodType.methodType(Object.class, String.class)))
                .orElse(null);
            PACKET_PARTICLES_CONSTRUCTOR = UncheckedHandles.findConstructor(lookup, PACKET_PLAY_OUT_PARTICLES,
                    MethodType.methodType(void.class, ENUM_PARTICLE, boolean.class, float.class, float.class, float.class,
                        float.class, float.class, float.class, float.class, int.class, int[].class))
                .map(handle -> handle.asType(MethodType.methodType(Object.class, Object.class, boolean.class,
                    float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class)))
                .orElse(null);
        } else {
            VALUE_OF_ENUM_PARTICLE = null;
            PACKET_PARTICLES_CONSTRUCTOR = null;
        }
    }

    private ParticlePacketAdapterImpl() {}

    @NotNull
    public static ParticlePacketAdapter getInstance() {
        return INSTANCE;
    }

    private Object getEnum_v1_8(final ParticleEnum particleEnum) throws Throwable {
        if(particleEnum == null) return null;
        return Objects.requireNonNull(VALUE_OF_ENUM_PARTICLE).invokeExact(particleEnum.name());
    }

    private Object getEnum_v1_9(final ParticleEnum particleEnum) throws Throwable {
        if(particleEnum == null) return null;
        return Objects.requireNonNull(VALUE_OF_BUKKIT_PARTICLE).invokeExact(particleEnum.name());
    }

    private void sendPacket(final @NotNull Player player, final @NotNull ParticleEffect effect) throws Throwable {
        final Object particle = getEnum_v1_8(effect.effect);
        if(particle == null) return;

        final Object packet = Objects.requireNonNull(PACKET_PARTICLES_CONSTRUCTOR).invokeExact(particle, effect.longDistance,
            (float) effect.x, (float) effect.y, (float) effect.z, effect.offsetX, effect.offsetY, effect.offsetZ, effect.particleData, effect.particleCount, effect.data);
        UncheckedReflects.sendPacket(player, packet);
    }

    private void spawnParticle(final @NotNull Player player, final @NotNull ParticleEffect effect) throws Throwable {
        final Object particle = getEnum_v1_9(effect.effect);
        if(particle == null) return;

        Objects.requireNonNull(SPAWN_PARTICLE).invokeExact(player, particle, effect.x, effect.y, effect.z, effect.particleCount,
            (double) effect.offsetX, (double) effect.offsetY, (double) effect.offsetZ, (double) effect.particleData, effect.data);
    }

    @Override
    public void send(final @NotNull Player player, final @NotNull ParticleEffect effect) {
        try {
            if(MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_9_R1))
                spawnParticle(player, effect);
            else sendPacket(player, effect);
        } catch(final Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
