package dev.hawu.plugins.api.nbt;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an abstract registry for converting from this API's
 * types of tags into Minecraft's and vice versa.
 *
 * @since 1.0
 */
public abstract class NBTRegistry {

    private static NBTRegistry registry;

    /**
     * Retrieves the computed registry for converting named binary tags.
     *
     * @return The computed registry instance that is never null.
     * @throws RuntimeException If the registry instance could not be found.
     * @since 1.0
     */
    @NotNull
    public static NBTRegistry getRegistry() {
        if(registry == null)
            return registry = new SimpleNBTRegistry();
        else return registry;
    }

    /**
     * Retrieves the Minecraft's tag held in the Bukkit's {@link ItemStack} instance.
     *
     * @param item The item whose tag to retrieve.
     * @return The tag in the item, can be null.
     * @since 1.0
     */
    @Nullable
    public abstract NBTCompound getCompound(@NotNull final ItemStack item);

    /**
     * Attempts to apply an instance of compound to the item.
     *
     * @param item     The item whose tag to override.
     * @param compound The compound to apply.
     * @return The new copy of the original item, that should have the compound applied.
     * @since 1.0
     */
    @NotNull
    public abstract ItemStack applyCompound(@NotNull final ItemStack item, @Nullable final NBTCompound compound);

}
