package dev.hawu.plugins.api.misc;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * The result of a raytracing operation.
 *
 * @since 1.6
 */
public final class RaytraceResult {

    private final List<Entity> entities;
    private final List<Block> blocks;
    private final Location location;

    RaytraceResult(final List<Entity> entities, final List<Block> blocks, final Location currentLocation) {
        this.entities = entities;
        this.blocks = blocks;
        this.location = currentLocation;
    }

    /**
     * Retrieves the set of all entities the operation has passed through.
     *
     * @return said set
     * @since 1.6
     */
    @NotNull
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Retrieves the set of blocks the operation has passed through.
     *
     * @return said set
     * @since 1.6
     */
    @NotNull
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Retrieves the current location of the operation.
     *
     * @return said location
     * @since 1.6
     */
    @NotNull
    public Location getCurrentLocation() {
        return location;
    }

}
