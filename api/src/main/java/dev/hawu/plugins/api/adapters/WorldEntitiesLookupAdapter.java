package dev.hawu.plugins.api.adapters;

import dev.hawu.plugins.api.exceptions.AlreadyInitializedException;
import dev.hawu.plugins.api.exceptions.LookupException;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * The class for looking up entities around a location.
 * Will only work properly for 1.9+ because doing it in 1.8 is very
 * performance heavy.
 *
 * @since 1.6
 */
public abstract class WorldEntitiesLookupAdapter {

    private static WorldEntitiesLookupAdapter adapter;

    /**
     * Gets entities around a location.
     *
     * @param location the location to look around.
     * @param x        the x-axis distance.
     * @param y        the y-axis distance.
     * @param z        the z-axis distance.
     * @return the entities.
     * @since 1.6
     */
    @NotNull
    public abstract Collection<Entity> getNearbyEntities(final @NotNull Location location, final double x, final double y, final double z);

    /**
     * Gets the entities around a location.
     *
     * @param location the location to look around
     * @param vector   the vector containing the radii
     * @return the entities
     * @since 1.6
     */
    @NotNull
    public abstract Collection<Entity> getNearbyEntities(final @NotNull Location location, final @NotNull Vector vector);

    /**
     * Gets the adapter.
     *
     * @return the adapter.
     * @since 1.6
     */
    @NotNull
    public static WorldEntitiesLookupAdapter getAdapter() {
        try {
            if(adapter == null) {
                final String version = MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_8_R2) ? "v1_8_R2" : MinecraftVersion.getCurrent().name();
                return adapter = (WorldEntitiesLookupAdapter) Class.forName("dev.hawu.plugins.api." + version + ".SimpleWorldEntitiesLookupAdapter")
                    .getConstructor().newInstance();
            } else return adapter;
        } catch(final Exception exception) {
            throw new LookupException(exception);
        }
    }

    /**
     * Initializes the adapter.
     *
     * @param lookupAdapter the adapter to use.
     * @since 1.6
     */
    public static void setAdapter(@NotNull WorldEntitiesLookupAdapter lookupAdapter) {
        if(adapter != null) throw new AlreadyInitializedException("World Entities Lookup adapter is already initialized.");
        adapter = lookupAdapter;
    }

}
