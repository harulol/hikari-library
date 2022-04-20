package dev.hawu.plugins.api.impl;

import dev.hawu.plugins.api.misc.WorldEntitiesLookupAdapter;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import dev.hawu.plugins.api.reflect.UncheckedHandles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public final class WorldEntitiesLookupAdapterImpl extends WorldEntitiesLookupAdapter {

    private static final WorldEntitiesLookupAdapterImpl instance = new WorldEntitiesLookupAdapterImpl();
    private static final MethodHandle GET_NEARBY_ENTITIES;

    static {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();

        GET_NEARBY_ENTITIES = UncheckedHandles.findVirtual(lookup, World.class, "getNearbyEntities",
            MethodType.methodType(Collection.class, Location.class, double.class, double.class, double.class))
            .orNull();
    }

    @NotNull
    public static WorldEntitiesLookupAdapter getInstance() {
        return instance;
    }

    private WorldEntitiesLookupAdapterImpl() {}

    @SuppressWarnings("unchecked")
    private Collection<Entity> getEntities(final @NotNull Location location, final double x, final double y, final double z) throws Throwable {
        Objects.requireNonNull(location.getWorld(), "World must be non-null.");
        final World world = location.getWorld();
        return (Collection<Entity>) Objects.requireNonNull(GET_NEARBY_ENTITIES).invokeExact(world, location, x, y, z);
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(final @NotNull Location location, final double x, final double y, final double z) {
        try {
            if(MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_9_R1))
                return getEntities(location, x, y, z);
        } catch(final Throwable e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        throw new UnsupportedOperationException("This method is not supported by your Minecraft version.");
    }

    @Override
    public @NotNull Collection<Entity> getNearbyEntities(final @NotNull Location location, final @NotNull Vector vector) {
        return getNearbyEntities(location, vector.getX(), vector.getY(), vector.getZ());
    }

}
