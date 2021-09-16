package dev.hawu.plugins.api.inventories.item;

import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

/**
 * Represents a unique collection of possible {@link ItemFlag}s
 * linked to an item stack, but with a builder pattern.
 *
 * @since 1.0
 */
public final class ItemFlagBuilder {

    private static final ItemFlag HIDE_DYE;

    static {
        ItemFlag value;
        try {
            value = ItemFlag.valueOf("HIDE_DYE");
        } catch(final IllegalArgumentException exception) {
            value = null;
        }
        HIDE_DYE = value;
    }

    private final ItemMetaBuilder hook;
    private final Set<ItemFlag> set;

    ItemFlagBuilder(@NotNull final ItemMetaBuilder caller) {
        this.hook = caller;
        this.set = caller.getFlags();
    }

    /**
     * Makes sure that the flags being assigned
     * don't contain {@link ItemFlag#HIDE_ATTRIBUTES}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder showAttributes() {
        set.remove(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * don't contain {@link ItemFlag#HIDE_ENCHANTS}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder showEnchants() {
        set.remove(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * don't contain {@link ItemFlag#HIDE_PLACED_ON}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder showPlacedOn() {
        set.remove(ItemFlag.HIDE_PLACED_ON);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * don't contain {@link ItemFlag#HIDE_DESTROYS}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder showDestroys() {
        set.remove(ItemFlag.HIDE_DESTROYS);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * don't contain {@link ItemFlag#HIDE_UNBREAKABLE}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder showUnbreakable() {
        set.remove(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * don't contain {@link ItemFlag#HIDE_POTION_EFFECTS}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder showPotionEffects() {
        set.remove(ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * have to contain {@link ItemFlag#HIDE_ATTRIBUTES}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder hideAttributes() {
        set.add(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * have to contain {@link ItemFlag#HIDE_ENCHANTS}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder hideEnchants() {
        set.add(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * have to contain {@link ItemFlag#HIDE_PLACED_ON}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder hidePlacedOn() {
        set.add(ItemFlag.HIDE_PLACED_ON);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * have to contain {@link ItemFlag#HIDE_DESTROYS}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder hideDestroys() {
        set.add(ItemFlag.HIDE_DESTROYS);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * have to contain {@link ItemFlag#HIDE_UNBREAKABLE}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder hideUnbreakable() {
        set.add(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    /**
     * Makes sure that the flags being assigned
     * have to contain {@link ItemFlag#HIDE_POTION_EFFECTS}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder hidePotionEffects() {
        set.add(ItemFlag.HIDE_POTION_EFFECTS);
        return this;
    }

    /**
     * Makes sure that the item has to have its dye
     * values hidden.
     * <p>
     * This should fail silently for earlier versions
     * when {@code ItemFLag.HIDE_DYE} does not exist yet.
     *
     * @return The same receiver
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder hideDye() {
        if(HIDE_DYE != null) set.add(HIDE_DYE);
        return this;
    }

    /**
     * Makes sure that the item has to have its dye
     * values shown.
     * <p>
     * This should fail silently for earlier versions
     * when {@code ItemFlag.HIDE_DYE} does not exist yet.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder showDye() {
        if(HIDE_DYE != null) set.remove(HIDE_DYE);
        return this;
    }

    /**
     * Appends all tags available in {@link ItemFlag}.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemFlagBuilder hideAll() {
        set.addAll(Arrays.asList(ItemFlag.values()));
        return this;
    }

    /**
     * Removes all tags currently available.
     *
     * @return The same receiver.
     * @since 1.0
     */
    public ItemFlagBuilder showAll() {
        set.clear();
        return this;
    }

    /**
     * Builds and retrieves back the {@link ItemMetaBuilder}.
     *
     * @return The hooked {@link ItemMetaBuilder}.
     * @since 1.0
     */
    @NotNull
    public ItemMetaBuilder build() {
        return hook.withFlags(set.toArray(new ItemFlag[0]));
    }

}
