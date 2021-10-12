package dev.hawu.plugins.api.holograms;

import dev.hawu.plugins.api.Strings;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a floating text using invisible armor stands
 * for displaying holographic stuff.
 * <p>
 * This class was made for prototyping only.
 *
 * @since 0.1
 */
@ApiStatus.Experimental
public final class Hologram {

    private final List<String> lines;
    private final List<ArmorStand> armorStands;
    private Location base;
    private double lineBreak = 0.3;

    /**
     * Constructs a bare hologram using a location base.
     *
     * @param location The location to create at.
     * @since 0.1
     */
    public Hologram(final @NotNull Location location) {
        this.base = location;
        this.lines = new ArrayList<>();
        this.armorStands = new ArrayList<>();
    }

    /**
     * Sets the amount in co-ords to separate lines.
     *
     * @param lineBreak The separation between lines' size.
     * @since 0.1
     */
    public void setLineBreak(final double lineBreak) {
        this.lineBreak = lineBreak;
    }

    /**
     * Adds multiple lines to this hologram.
     *
     * @param s The lines to add.
     * @since 0.1
     */
    public void addLines(final @NotNull String @NotNull ... s) {
        lines.addAll(Arrays.stream(s).collect(Collectors.toList()));
    }

    /**
     * Creates armor stand objects and renders the text
     * live in game.
     *
     * @since 0.1
     */
    public void render() {
        final Location loc = base.clone();

        armorStands.forEach(ArmorStand::remove);
        armorStands.clear();

        lines.forEach(s -> {
            final ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            as.setGravity(false);
            as.setCanPickupItems(false);
            as.setCustomNameVisible(true);
            as.setCustomName(Strings.color(s));
            armorStands.add(as);
            loc.subtract(0.0, lineBreak, 0.0);
        });
    }

}
