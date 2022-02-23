package dev.hawu.plugins.api.particles;

import dev.hawu.plugins.api.MathUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A class dedicated to providing pre-defined
 * configurations of particle effects.
 *
 * @since 1.5
 */
public final class PredefParticles {

    private PredefParticles() {}

    /**
     * Draws a circle lying in the x-z plane.
     *
     * @param radius  the radius of the circle
     * @param step    the step size (in radians)
     * @param center  the center of the circle
     * @param effect  the effect to use
     * @param players the players that can see the circle
     * @since 1.5
     */
    public static void drawFlatCircle(final double radius, final double step, final @NotNull Location center,
                                      final @NotNull ParticleEffect effect, final @NotNull List<@NotNull Player> players) {
        for(double angle = 0; angle < 2 * Math.PI; angle += step) {
            final double x = radius * Math.cos(angle);
            final double z = radius * Math.sin(angle);
            final Location loc = center.clone().add(x, 0, z);
            players.forEach(effect::play);
        }
    }

    /**
     * Draws a line between two points.
     *
     * @param from    the start point
     * @param to      the end point
     * @param step    the step size
     * @param effect  the effect to use
     * @param players the players that can see the line
     * @since 1.5
     */
    public static void drawLine(final @NotNull Location from, final @NotNull Location to, final double step,
                                final @NotNull ParticleEffect effect, final @NotNull List<@NotNull Player> players) {
        final Vector current = from.toVector();
        final Vector destination = to.toVector();
        final Vector direction = MathUtils.normalize(to.toVector().subtract(from.toVector())).multiply(step);
        while(current.distanceSquared(destination) > step) {
            players.forEach(effect::play);
            current.add(direction);
        }
    }

    /**
     * Generates a colored dust particle.
     *
     * @param location the location of the particle
     * @param red      the red value
     * @param green    the green value
     * @param blue     the blue value
     * @return the particle
     * @since 1.5
     */
    @NotNull
    public static ParticleEffect getColoredParticle(final @NotNull Location location, final double red, final double green, final double blue) {
        if(red > 255 || green > 255 || blue > 255) {
            throw new IllegalArgumentException("Color values must be between 0 and 255.");
        }

        return ParticleEffect.of(ParticleEnum.REDSTONE)
            .setLocation(location)
            .setParticleCount(0)
            .setOffsetX((float) (red == 0.0 ? 0.001 : red / 255.0))
            .setOffsetY((float) (green / 255.0))
            .setOffsetZ((float) (blue / 255.0))
            .setParticleData(1f)
            .build();
    }

}
