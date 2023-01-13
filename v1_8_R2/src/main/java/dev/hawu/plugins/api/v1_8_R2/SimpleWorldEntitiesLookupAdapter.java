package dev.hawu.plugins.api.v1_8_R2;

import dev.hawu.plugins.api.adapters.WorldEntitiesLookupAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Internal
public class SimpleWorldEntitiesLookupAdapter extends WorldEntitiesLookupAdapter {

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(final @NotNull Location location, final double x, final double y, final double z) {
        return location.getWorld().getNearbyEntities(location, x, y, z);
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(final @NotNull Location location, final @NotNull Vector vector) {
        return getNearbyEntities(location, vector.getX(), vector.getY(), vector.getZ());
    }

}
