package dev.hawu.plugins.api.inventories.item;

import dev.hawu.plugins.api.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * A class for dealing with displayable materials and item stacks
 * within widgets and inventories.
 *
 * @since 2.4
 */
public final class BukkitMaterial {

    private static final Set<String> EXPLICITLY_IGNORED = new HashSet<>();
    private static final Set<Material> DISPLAYABLE = new HashSet<>();
    private static final Set<Material> CANT_DISPLAY = new HashSet<>();

    private static Set<Material> UNMODIFIABLE_DISPLAYABLE;
    private static Set<Material> UNMODIFIABLE_CANT_DISPLAY;

    static {
        // Adds the two materials that cant display in 1.8
        EXPLICITLY_IGNORED.add("SOIL");
        EXPLICITLY_IGNORED.add("BURNING_FURNACE");
    }

    private BukkitMaterial() {}

    /**
     * Attempts to reinitialize all the collections by clearing them,
     * and recollecting them again.
     *
     * @since 2.4
     */
    public static void reInitDisplayableMaterials() {
        if(!DISPLAYABLE.isEmpty() || !CANT_DISPLAY.isEmpty()) {
            Constants.getPlugin().getLogger().log(Level.WARNING, "Reinitializing the displayable cache while already initialized.");
        }
        DISPLAYABLE.clear();
        CANT_DISPLAY.clear();

        final Inventory dummy = Bukkit.createInventory(null, 9);
        Arrays.stream(Material.values())
            .filter(mat -> mat.name().startsWith("LEGACY_"))
            .filter(mat -> !EXPLICITLY_IGNORED.contains(mat.name()))
            .forEach(mat -> {
                dummy.setItem(0, new ItemStack(mat));
                if(dummy.getItem(0) == null || dummy.getItem(0).getType().name().endsWith("AIR")) CANT_DISPLAY.add(mat);
                else DISPLAYABLE.add(mat);
            });

        UNMODIFIABLE_DISPLAYABLE = Collections.unmodifiableSet(DISPLAYABLE);
        UNMODIFIABLE_CANT_DISPLAY = Collections.unmodifiableSet(CANT_DISPLAY);
    }

    /**
     * Retrieves a non-modifiable view of the set that contains
     * all materials that are deemed display-able.
     *
     * @return The underlying unmodifiable set.
     * @since 2.4
     */
    @NotNull
    public static Set<@NotNull Material> getDisplayables() {
        return UNMODIFIABLE_DISPLAYABLE;
    }

    /**
     * Retrieves a non-modifiable view of the set that contains
     * all materials that are deemed un-displayable.
     *
     * @return The underlying unmodifiable set.
     * @since 2.4
     */
    @NotNull
    public static Set<@NotNull Material> getUndisplayables() {
        return UNMODIFIABLE_CANT_DISPLAY;
    }

}
