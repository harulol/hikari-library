package dev.hawu.plugins.api.v1_8_R1;

import dev.hawu.plugins.api.adapters.WorldEntitiesLookupAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Internal
public class SimpleWorldEntitiesLookupAdapter extends WorldEntitiesLookupAdapter {

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(final @NotNull Location location, final double x, final double y, final double z) {
        // Have to create a dummy because location doesn't support looking up yet.
        final Egg egg = (Egg) location.getWorld().spawnEntity(location, EntityType.EGG);
        final Collection<Entity> entities = egg.getNearbyEntities(x, y, z);
        egg.remove();
        return entities;
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(final @NotNull Location location, final @NotNull Vector vector) {
        return getNearbyEntities(location, vector.getX(), vector.getY(), vector.getZ());
    }

}
