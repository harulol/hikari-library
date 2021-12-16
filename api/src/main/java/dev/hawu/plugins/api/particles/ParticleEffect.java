package dev.hawu.plugins.api.particles;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

/**
 * The contained particle effect that can be used to send
 * to a player, or multiple players through a particle registry.
 *
 * @since 1.0
 */
public final class ParticleEffect {

    /**
     * Represents the name of the particle effect
     * that will be passed in.
     *
     * @since 1.0
     */
    @Deprecated
    public final String particle;

    /**
     * Represents the particle effect.
     *
     * @since 1.3
     */
    public final ParticleEnum effect;

    /**
     * Whether this particle can be viewed by players
     * far away.
     *
     * @since 1.0
     */
    public final boolean longDistance;

    /**
     * The value on the X axis that contributes
     * to the location of the particle.
     *
     * @since 1.0
     */
    public final double x;

    /**
     * The value on the Y axis that contributes
     * to the location of the particle.
     *
     * @since 1.0
     */
    public final double y;

    /**
     * The value on the Z axis that contributes
     * to the location of the particle.
     *
     * @since 1.0
     */
    public final double z;

    /**
     * The value to multiply with after an invocation
     * of {@link Random#nextGaussian()} for the X axis.
     *
     * @since 1.0
     */
    public final float offsetX;

    /**
     * The value to multiply with after an invocation
     * of {@link Random#nextGaussian()} for the Y axis.
     *
     * @since 1.0
     */
    public final float offsetY;

    /**
     * The value to multiply with after an invocation
     * of {@link Random#nextGaussian()} for the Z axis.
     *
     * @since 1.0
     */
    public final float offsetZ;

    /**
     * The data of the particle, usually the speed.
     *
     * @since 1.0
     */
    public final float particleData;

    /**
     * The number of particles to display within
     * one packet.
     *
     * @since 1.0
     */
    public final int particleCount;

    /**
     * Additional data for the particle, for example,
     * colors for dust particles or materials for block particles.
     *
     * @since 1.0
     */
    public final int[] data;

    /**
     * Constructs a contained packet that displays a named particle.
     *
     * @param particle      The name of the particle.
     * @param longDistance  If true, the particle distance increased from 256 to 65536.
     * @param x             The X position of the particle.
     * @param y             The Y position of the particle.
     * @param z             The Z position of the particle.
     * @param offsetX       The random offset of the particle on the X axis.
     * @param offsetY       The random offset of the particle on the Y axis.
     * @param offsetZ       The random offset of the particle on the Z axis.
     * @param particleData  The data of each particle.
     * @param particleCount The number of particles to create.
     * @param data          The variable data of each type of particle.
     * @since 1.0
     */
    @Deprecated
    public ParticleEffect(final @NotNull String particle, final boolean longDistance, final double x, final double y, final double z,
                          final float offsetX, final float offsetY, final float offsetZ,
                          final float particleData, final int particleCount, final int @NotNull ... data) {
        this(ParticleEnum.getParticle(particle), longDistance, x, y, z,
            offsetX, offsetY, offsetZ, particleData, particleCount, data);
    }

    /**
     * Constructs a contained packet that displays a named particle.
     *
     * @param effect        The particle enum.
     * @param longDistance  If true, the particle distance increased from 256 to 65536.
     * @param x             The X position of the particle.
     * @param y             The Y position of the particle.
     * @param z             The Z position of the particle.
     * @param offsetX       The random offset of the particle on the X axis.
     * @param offsetY       The random offset of the particle on the Y axis.
     * @param offsetZ       The random offset of the particle on the Z axis.
     * @param particleData  The data of each particle.
     * @param particleCount The number of particles to create.
     * @param data          The variable data of each type of particle.
     * @since 1.0
     */
    public ParticleEffect(final @Nullable ParticleEnum effect, final boolean longDistance, final double x, final double y, final double z,
                          final float offsetX, final float offsetY, final float offsetZ,
                          final float particleData, final int particleCount, final int @NotNull ... data) {
        this.effect = effect;
        this.particle = Objects.toString(effect);
        this.longDistance = longDistance;
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.particleData = particleData;
        this.particleCount = particleCount;
        this.data = data;
    }

    /**
     * Constructs a new builder for a particle effect.
     *
     * @param particle The type of the particle for this effect.
     * @return A new instance of {@link ParticleEffectBuilder}.
     * @since 1.0
     */
    @NotNull
    @Deprecated
    public static ParticleEffectBuilder of(final @NotNull String particle) {
        return new ParticleEffectBuilder(particle);
    }

    /**
     * Constructs a new builder for a particle effect.
     *
     * @param particle The type of the particle for this effect.
     * @return A new instance of {@link ParticleEffectBuilder}.
     * @since 1.3
     */
    @NotNull
    public static ParticleEffectBuilder of(final @NotNull ParticleEnum particle) {
        return new ParticleEffectBuilder(particle);
    }

}
