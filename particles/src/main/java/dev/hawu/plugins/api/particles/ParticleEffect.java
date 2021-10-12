package dev.hawu.plugins.api.particles;

import org.jetbrains.annotations.NotNull;

/**
 * The contained particle effect that can be used to send
 * to a player, or multiple players through a particle registry.
 *
 * @since 1.0
 */
public final class ParticleEffect {

    public final String particle;
    public final boolean longDistance;
    public final double x;
    public final double y;
    public final double z;
    public final float offsetX;
    public final float offsetY;
    public final float offsetZ;
    public final float particleData;
    public final int particleCount;
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
    public ParticleEffect(final @NotNull String particle, final boolean longDistance, final double x, final double y, final double z,
                          final float offsetX, final float offsetY, final float offsetZ,
                          final float particleData, final int particleCount, final int @NotNull ... data) {
        this.particle = particle;
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
    public static ParticleEffectBuilder of(final @NotNull String particle) {
        return new ParticleEffectBuilder(particle);
    }

}
