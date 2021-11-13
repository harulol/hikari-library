package dev.hawu.plugins.api.items;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.nbt.NBTCompound;
import dev.hawu.plugins.api.nbt.NBTRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The builder for creating item stacks using
 * a builder pattern cleanly.
 *
 * @since 1.2
 */
public final class ItemStackBuilder {

    private final Material material;
    private final ItemMeta meta;

    private int amount = 1;
    private short durability = 0;
    private byte data = -1;
    private NBTCompound compound = null;

    private ItemStackBuilder(final @NotNull Material material) {
        this.material = material;
        this.meta = Bukkit.getItemFactory().getItemMeta(material);
    }

    @SuppressWarnings("deprecation")
    private ItemStackBuilder(final @NotNull ItemStack itemStack) {
        this.material = itemStack.getType();
        this.meta = itemStack.getItemMeta();
        this.amount = itemStack.getAmount();
        this.durability = itemStack.getDurability();
        this.data = itemStack.getData().getData();
    }

    /**
     * Constructs a new item stack builder
     * using the material.
     *
     * @param material The material of the new item stack.
     * @return The newly created item stack builder.
     * @since 1.2
     */
    @NotNull
    public static ItemStackBuilder of(final @NotNull Material material) {
        return new ItemStackBuilder(material);
    }

    /**
     * Constructs a new item stack builder
     * from a base stack.
     *
     * @param base The item to create from.
     * @return The newly created item stack builder.
     * @since 1.2
     */
    @NotNull
    public static ItemStackBuilder from(final @NotNull ItemStack base) {
        return new ItemStackBuilder(base);
    }

    /**
     * Sets the amount for this item in this builder.
     *
     * @param amount The amount of this item stack.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder amount(final int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the durability for this item in this builder.
     *
     * @param durability The durability of this item stack.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder durability(final short durability) {
        this.durability = durability;
        return this;
    }

    /**
     * Sets the data for this item in this builder.
     *
     * @param data The data of this item stack.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    @Deprecated
    public ItemStackBuilder data(final byte data) {
        this.data = data;
        return this;
    }

    /**
     * Sets the display name for this item in the builder,
     * colorized automatically.
     *
     * @param name The name to set.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder name(final @Nullable String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    /**
     * Sets the lore for this item in the builder, all
     * colorized automatically.
     *
     * @param lore The lore to set.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder lore(final @NotNull Collection<@NotNull String> lore) {
        this.meta.setLore(lore.stream().map(Strings::color).collect(Collectors.toList()));
        return this;
    }

    /**
     * Sets the lore for this item in the builder by lines,
     * all colorized automatically.
     *
     * @param lore The new lore to set.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder lore(final @NotNull String... lore) {
        return this.lore(Arrays.asList(lore));
    }

    /**
     * Sets a specific line at the provided index of the lore
     * to the new line, colorized.
     * <p>
     * This fails silently if the item currently has no lore
     * or the index is out of bounds.
     *
     * @param index The index to set.
     * @param line  The line to set to.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder loreLine(final int index, final @NotNull String line) {
        final List<String> lore = this.meta.hasLore() ? this.meta.getLore() : new ArrayList<>();
        if(index >= 0 && index < lore.size()) {
            lore.set(index, Strings.color(line));
        }
        this.meta.setLore(lore);
        return this;
    }

    /**
     * Makes sure that the current item only has the item flags
     * specified, and no other.
     *
     * @param flags The only flags that this item should have.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder flags(final @NotNull ItemFlag @NotNull ... flags) {
        this.meta.removeItemFlags(ItemFlag.values());
        this.meta.addItemFlags(flags);
        return this;
    }

    /**
     * Enchants the item with the provided enchantment at the provided level.
     *
     * @param enchantment The enchantment to enchant with.
     * @param level       The level of the enchantment.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder enchant(final @NotNull Enchantment enchantment, final int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Removes the enchantment from the item.
     *
     * @param enchantment The enchantment to remove.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder disenchant(final @NotNull Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Enchants the item stack and an enchantment-hiding flag
     * to simulate the item glowing.
     *
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder glow() {
        this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this.enchant(Enchantment.DURABILITY, 1);
    }

    /**
     * Applies a function to make changes to the item meta
     * directly.
     *
     * @param metaConsumer The consumer to accept.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder transform(final @NotNull Consumer<@NotNull ItemMeta> metaConsumer) {
        metaConsumer.accept(this.meta);
        return this;
    }

    /**
     * Sets the compound to be applied to the item stack
     * after the meta is applied.
     *
     * @param compound The compound to apply.
     * @return The same builder.
     * @since 1.2
     */
    @NotNull
    public ItemStackBuilder compound(final @Nullable NBTCompound compound) {
        if(this.compound == null) this.compound = compound;
        else if(compound != null) this.compound.putAll(compound);
        return this;
    }

    /**
     * Constructs the item stacks with the values provided.
     *
     * @return The item stack.
     * @since 1.2
     */
    @NotNull
    @SuppressWarnings("deprecation")
    public ItemStack build() {
        final ItemStack itemStack;
        if(this.data >= 0)
            itemStack = new ItemStack(material, amount, durability, data);
        else itemStack = new ItemStack(material, amount, durability);

        itemStack.setItemMeta(this.meta);
        final NBTCompound compound = NBTRegistry.getRegistry().getCompound(itemStack);
        Objects.requireNonNull(compound).putAll(this.compound);
        return NBTRegistry.getRegistry().applyCompound(itemStack, compound);
    }

}
