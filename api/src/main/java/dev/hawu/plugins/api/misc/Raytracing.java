package dev.hawu.plugins.api.misc;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * Raytracing stuff, i guess?
 *
 * @since 1.6
 */
public class Raytracing {

    private Location origin;
    private Vector direction;
    private boolean ignoresBlocks = false;
    private double step = 1.0;
    private double distance = 10.0;
    private Consumer<Location> eachStep = null;

    protected Raytracing() {
    }

    /**
     * Creates a new builder for a [Raytracing].
     *
     * @return said builder
     * @since 1.6
     */
    @NotNull
    public static Raytracing startNew() {
        return new Raytracing();
    }

    /**
     * Configures where the raytracing should start.
     *
     * @param origin the origin
     * @return this builder
     * @since 1.6
     */
    @NotNull
    public final Raytracing origin(final @NotNull Location origin) {
        this.origin = origin;
        return this;
    }

    /**
     * Configures the direction of the raytracing.
     *
     * @param direction the direction
     * @return this builder
     * @since 1.6
     */
    @NotNull
    public final Raytracing direction(final @NotNull Vector direction) {
        this.direction = direction;
        return this;
    }

    /**
     * Configures that the raytracing should ignore solid blocks.
     *
     * @return this builder
     * @since 1.6
     */
    @NotNull
    public final Raytracing ignoresBlocks() {
        ignoresBlocks = true;
        return this;
    }

    /**
     * Configures the step. The smaller the step, the more precise the raytracing.
     * <p>
     * If the step is too small, it may strain the server.
     *
     * @param step the step
     * @return this builder
     * @since 1.6
     */
    @NotNull
    public final Raytracing step(final double step) {
        this.step = step;
        return this;
    }

    /**
     * Configures the distance, how far the raytracing should travel before stopping.
     * <p>
     * If the distance is too large, it may strain the server.
     *
     * @param distance the distance
     * @return this builder
     * @since 1.6
     */
    @NotNull
    public final Raytracing distance(final double distance) {
        this.distance = distance;
        return this;
    }

    /**
     * Configures the consumer that is called for each step.
     *
     * @param eachStep the consumer
     * @return this builder
     * @since 1.6
     */
    @NotNull
    public final Raytracing eachStep(final Consumer<Location> eachStep) {
        this.eachStep = eachStep;
        return this;
    }

    /**
     * Starts the raytrace.
     *
     * @return the result
     * @since 1.6
     */
    @NotNull
    public final RaytraceResult raytrace() {
        Objects.requireNonNull(origin, "\"origin\" must not be null.");
        Objects.requireNonNull(direction, "\"direction\" must not be null.");
        Preconditions.checkState(direction.lengthSquared() > 0.0, "\"direction\" must not be a zero vector.");
        Preconditions.checkState(step > 0.0, "\"step\" must be greater than zero.");
        Preconditions.checkState(distance > 0.0, "\"distance\" must be greater than zero.");

        direction.normalize().multiply(step);

        final double squaredDistance = distance * distance;
        final Location current = origin.clone();
        final Set<UUID> visitedEntities = new HashSet<>();
        final Set<Entity> entities = new HashSet<>();
        final Set<Vector> visitedBlocks = new HashSet<>();
        final List<Block> blocks = new ArrayList<>();

        while(true) {
            if(current.distanceSquared(origin) >= squaredDistance)
                break;

            // Adds the block only if it is not visited.
            final Block block = current.getBlock();
            if(!visitedBlocks.contains(block.getLocation().toVector())) {
                visitedBlocks.add(block.getLocation().toVector());
                blocks.add(block);
            }

            // Checks for entities. We can't use .getNearbyEntities since 1.8 does not have it implemented.
            current.getWorld().getEntities().stream()
                .filter(entity -> entity.getLocation().distanceSquared(current) <= step * step)
                .forEach(entity -> {
                    if(!visitedEntities.contains(entity.getUniqueId())) {
                        visitedEntities.add(entity.getUniqueId());
                        entities.add(entity);
                    }
                });

            // Whatever needs to be done on each step.
            if(eachStep != null)
                eachStep.accept(current.clone());

            // Break the operation if it hits a block and the raytracing should take blocks into account.
            if(block.getType().isSolid() && !ignoresBlocks) break;

            current.add(direction);
        }

        return new RaytraceResult(entities, blocks, current.subtract(direction));
    }

}
