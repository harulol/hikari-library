package dev.hawu.plugins.api.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Random miscellaneous class for Bukkit Materials.
 *
 * @since 1.2
 */
public final class BukkitMaterial {

    private static final List<Material> DISPLAYABLE_MATERIALS = new ArrayList<>();

    // These things show up without a texture in Bukkit 1.8, just ignoring them.
    private static final Set<String> IGNORED = Stream.of("SOIL", "BURNING_FURNACE").collect(Collectors.toSet());

    private BukkitMaterial() {}

    /**
     * Initializes the materials that can be displayed
     * within inventories.
     *
     * @since 1.2
     */
    public static void initialize() {
        DISPLAYABLE_MATERIALS.clear();
        final Inventory inventory = Bukkit.createInventory(null, 9);
        for(final Material material : Material.values()) {
            // Make sure that the material is not ignored and not legacy (preventing duplicates)
            if(IGNORED.contains(material.name()) || material.name().startsWith("LEGACY_")) continue;
            inventory.setItem(0, new ItemStack(material));
            if(inventory.getItem(0) != null && inventory.getItem(0).getType() != Material.AIR) {
                DISPLAYABLE_MATERIALS.add(material);
            }
        }
    }

    /**
     * Retrieves the list of materials that can be displayed.
     *
     * @return Said list.
     * @since 1.2
     */
    @NotNull
    @UnmodifiableView
    public static List<@NotNull Material> getDisplayableMaterials() {
        return Collections.unmodifiableList(DISPLAYABLE_MATERIALS);
    }

}
