package dev.hawu.plugins.api.particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a builder for building a particle effect
 * with named arguments.
 *
 * @since 1.0
 */
public final class ParticleEffectBuilder {

    private final String particle;
    private boolean longDistance;
    private double x;
    private double y;
    private double z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float particleData;
    private int particleCount;
    private int[] data;

    ParticleEffectBuilder(final @NotNull String particle) {
        this.particle = particle;
    }

    /**
     * Configures whether this particle can be seen from far away.
     * <p>
     * Only effective from versions 1.8 to 1.8.8.
     *
     * @param value Whether players can see this particle from far away.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder longDistance(final boolean value) {
        this.longDistance = value;
        return this;
    }

    /**
     * Sets the X for this effect's base location.
     *
     * @param x The x value for this effect.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setX(final double x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the Y for this effect's base location.
     *
     * @param y The y value for this effect.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setY(final double y) {
        this.y = y;
        return this;
    }

    /**
     * Sets the Z for this effect's base location.
     *
     * @param z The z value for this effect.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setZ(final double z) {
        this.z = z;
        return this;
    }

    /**
     * Sets the location for this effect using coordinates
     * in one invocation.
     * <p>
     * Shorthand for {@code setX(x).setY(y).setZ(z)}.
     *
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder location(final double x, final double y, final double z) {
        return this.setX(x).setY(y).setZ(z);
    }

    /**
     * Sets the base location of this particle effect.
     *
     * @param location The base location.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setLocation(final @NotNull Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        return this;
    }

    /**
     * Sets the base location of this particle effect using a vector.
     *
     * @param vector The vector whose value to use.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setLocation(final @NotNull Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
        return this;
    }

    /**
     * Sets the offset on the X axis.
     *
     * @param offsetX The offset value.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setOffsetX(final float offsetX) {
        this.offsetX = offsetX;
        return this;
    }

    /**
     * Sets the offset on the Y axis.
     *
     * @param offsetY The offset value.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setOffsetY(final float offsetY) {
        this.offsetY = offsetY;
        return this;
    }

    /**
     * Sets the offset on the Z axis.
     *
     * @param offsetZ The offset value.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setOffsetZ(final float offsetZ) {
        this.offsetZ = offsetZ;
        return this;
    }

    /**
     * Sets the offset for this effect using the values presented in
     * a vector.
     *
     * @param vector The vector whose value to use.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setOffset(final @NotNull Vector vector) {
        this.offsetX = (float) vector.getX();
        this.offsetY = (float) vector.getY();
        this.offsetZ = (float) vector.getZ();
        return this;
    }

    /**
     * Configures the offset values of this effect in one invocation.
     * <p>
     * Shorthand for {@code setOffsetX(x).setOffsetY(y).setOffsetZ(z)}.
     *
     * @param x The x offset.
     * @param y The y offset.
     * @param z The z offset.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder offset(final float x, final float y, final float z) {
        return this.setOffsetX(x).setOffsetY(y).setOffsetZ(z);
    }

    /**
     * Sets the particle data.
     *
     * @param particleData The data of the particle.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setParticleData(final float particleData) {
        this.particleData = particleData;
        return this;
    }

    /**
     * Sets the number of particles to display.
     *
     * @param particleCount The amount of particles.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setParticleCount(final int particleCount) {
        this.particleCount = particleCount;
        return this;
    }

    /**
     * Sets the additional data for this particle effect.
     *
     * @param data The additional data.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ParticleEffectBuilder setData(final int @NotNull ... data) {
        this.data = data;
        return this;
    }

    /**
     * Constructs a particle effect from the provided values
     * in this builder instance.
     *
     * @return A non-null {@link ParticleEffect}.
     * @since 1.0
     */
    @NotNull
    public ParticleEffect build() {
        return new ParticleEffect(particle, longDistance, x, y, z,
            offsetX, offsetY, offsetY, particleData, particleCount, data);
    }

}
