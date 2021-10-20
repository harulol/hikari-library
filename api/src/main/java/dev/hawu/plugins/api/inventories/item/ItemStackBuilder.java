package dev.hawu.plugins.api.inventories.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a builder for an {@link ItemStack}
 * with named arguments.
 *
 * @since 1.0
 */
public final class ItemStackBuilder {

    private Material material = Material.AIR;
    private int amount = 1;
    private short durability = 0;
    private byte data = -1;

    private ItemMeta meta;

    /**
     * Constructs an empty builder for building item stacks
     * from scratch.
     *
     * @since 1.0
     */
    public ItemStackBuilder() {}

    /**
     * Constructs a builder from a base item stack with predefined
     * type, amount, durability and meta.
     *
     * @param item The item to base on.
     * @since 1.0
     */
    @SuppressWarnings("deprecation")
    public ItemStackBuilder(@NotNull final ItemStack item) {
        material = item.getType();
        amount = item.getAmount();
        durability = item.getDurability();
        data = item.getData().getData();
        meta = item.getItemMeta();
    }

    /**
     * Sets the value for the material parameter. Default to {@link Material#AIR}.
     *
     * @param mat The material of the item.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemStackBuilder material(@NotNull final Material mat) {
        this.material = mat;
        return this;
    }

    /**
     * Sets the number of items in the stack. Default to {@code 1}.
     *
     * @param value The amount of the stack.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemStackBuilder amount(final int value) {
        this.amount = value;
        return this;
    }

    /**
     * Sets the durability of the item, or the data of the item,
     * if the material does not have a durability. Default to {@code 0}.
     *
     * @param value The durability or the data of the item.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemStackBuilder durability(final short value) {
        this.durability = value;
        return this;
    }

    /**
     * Sets the magic byte value of the item. This has been deprecated
     * since {@code 1.8} or earlier.
     *
     * @param value The magic byte value.
     * @return The same receiver.
     * @since 1.0
     */
    @Deprecated
    @NotNull
    public ItemStackBuilder data(final byte value) {
        this.data = value;
        return this;
    }

    /**
     * Constructs an empty {@link ItemMeta} or uses the linked one
     * and initializes a {@link ItemMetaBuilder} for building
     * the meta.
     *
     * @return A new instance of {@link ItemMetaBuilder} linked to this.
     * @since 1.0
     */
    @NotNull
    public ItemMetaBuilder withMeta() {
        if(meta != null) return new ItemMetaBuilder(meta, this);
        else return new ItemMetaBuilder(meta = Bukkit.getItemFactory().getItemMeta(material), this);
    }

    /**
     * Overrides the value of the field meta in this builder scope.
     *
     * @param meta The new meta to override with.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public ItemStackBuilder withMeta(final ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    /**
     * Constructs the {@link ItemStack} from the provided values.
     * <p>
     * If {@link ItemStackBuilder#data(byte)} was passed a positive integer or {@code 0},
     * this will use the deprecated constructor {@link ItemStack#ItemStack(Material, int, short, Byte)}.
     * <p>
     * Otherwise, this will use the constructor that is not yet deprecated
     * in version {@code 1.8} {@link ItemStack#ItemStack(Material, int, short)}.
     *
     * @return An instance of {@link ItemStack}.
     * @since 1.0
     */
    @SuppressWarnings("deprecation")
    @NotNull
    public ItemStack build() {
        final ItemStack item = new ItemStack(material, amount);
        item.setDurability(durability);
        if(data >= 0) item.getData().setData(data);
        item.setItemMeta(meta);
        return item;
    }

}
