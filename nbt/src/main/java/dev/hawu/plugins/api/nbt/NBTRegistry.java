package dev.hawu.plugins.api.nbt;

import dev.hawu.plugins.api.reflect.MinecraftVersion;
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

    private static String relocationURL = "dev.hawu.plugins.api";
    private static NBTRegistry registry;

    /**
     * Retrieves the newly set relocation URL of this class.
     * Implementation classes are looked up via {@code relocationURL + ".VERSION.SimpleNBTRegistry"}.
     *
     * @return The set relocation URL.
     * @since 1.0
     */
    @NotNull
    public static String getRelocationURL() {
        return relocationURL;
    }

    /**
     * Sets the new value of the relocation URL, so class lookups may be accurate.
     *
     * @param relocationURL The new relocated URL of the class.
     * @since 1.0
     */
    public static void setRelocationURL(@NotNull final String relocationURL) {
        NBTRegistry.relocationURL = relocationURL;
    }

    /**
     * Retrieves the computed registry for converting named binary tags.
     *
     * @return The computed registry instance that is never null.
     * @throws RuntimeException If the registry instance could not be found.
     * @since 1.0
     */
    @NotNull
    public static NBTRegistry getRegistry() {
        try {
            if(registry == null)
                return registry = (NBTRegistry) Class.forName(relocationURL + "." + MinecraftVersion.getCurrent().name() + ".SimpleNBTRegistry").newInstance();
            else return registry;
        } catch(final ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
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
