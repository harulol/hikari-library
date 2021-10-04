package dev.hawu.plugins.api.inventories.item;

import dev.hawu.plugins.api.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Represents a builder for building {@link ItemMeta}s
 * by chaining method invocations.
 *
 * @since 1.0
 */
public class ItemMetaBuilder {

    private final ItemMeta meta;
    private final ItemStackBuilder hook;

    ItemMetaBuilder(@NotNull final ItemMeta from, @NotNull final ItemStackBuilder caller) {
        this.meta = from;
        this.hook = caller;
    }

    ItemMetaBuilder(@NotNull final Material material, @NotNull final ItemStackBuilder caller) {
        this.meta = Bukkit.getItemFactory().getItemMeta(material);
        this.hook = caller;
    }

    /**
     * Applies a transformer in a form of a {@link Consumer} to the
     * {@link ItemMeta}.
     * <p>
     * This should be used when developing against a new version that
     * this library does not have native support for.
     *
     * @param consumer The transformer to apply.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public final ItemMetaBuilder transformMeta(@NotNull final Consumer<ItemMeta> consumer) {
        consumer.accept(meta);
        return this;
    }

    /**
     * Changes the display name of this item.
     *
     * @param name The name to change to, colorized.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public final ItemMetaBuilder displayName(@Nullable final String name) {
        meta.setDisplayName(name == null ? null : Strings.color(name));
        return this;
    }

    /**
     * Initializes and opens an {@link ItemFlagBuilder} with this meta's
     * item flags.
     *
     * @return A new instance of {@link ItemFlagBuilder}.
     * @since 1.0
     */
    @NotNull
    public final ItemFlagBuilder withFlags() {
        return new ItemFlagBuilder(this);
    }

    /**
     * Clears all of this meta's current flags and adds all
     * in the parameters.
     *
     * @param flags The only flags this meta should have.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public final ItemMetaBuilder withFlags(@NotNull final ItemFlag... flags) {
        meta.removeItemFlags(ItemFlag.values());
        meta.addItemFlags(flags);
        return this;
    }

    /**
     * Initializes and opens a builder that builds on the lore
     * of this meta.
     *
     * @return A new instance of {@link LoreBuilder}.
     * @since 1.0
     */
    @NotNull
    public final LoreBuilder withLore() {
        return new LoreBuilder(this);
    }

    /**
     * Sets the lore of this meta.
     *
     * @param lore The lore to set.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public final ItemMetaBuilder withLore(@Nullable final List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    /**
     * Sets the lore of this meta after colorizing all values
     * in the provided list.
     *
     * @param lore The lore to set.
     * @return The same receiver.
     * @since 2.2
     */
    @NotNull
    public final ItemMetaBuilder withColoredLore(@Nullable final List<String> lore) {
        if(lore == null) meta.setLore(null);
        else meta.setLore(lore.stream().map(Strings::color).collect(Collectors.toList()));
        return this;
    }

    /**
     * Adds an enchantment to this meta.
     *
     * @param enchantment The enchantment to add.
     * @param level       The level of the enchantment.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public final ItemMetaBuilder putEnchant(@NotNull final Enchantment enchantment, final int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Removes an enchantment from this meta.
     *
     * @param enchantment The enchantment to remove.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public final ItemMetaBuilder removeEnchant(@NotNull final Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Builds the meta and retrieves back the originally linked
     * {@link ItemStackBuilder}.
     *
     * @return The linked builder.
     * @since 1.0
     */
    @NotNull
    public final ItemStackBuilder build() {
        return hook.withMeta(meta);
    }

    final Set<ItemFlag> getFlags() {
        return meta.getItemFlags();
    }

    final List<String> getLore() {
        return meta.hasLore() ? meta.getLore() : new ArrayList<>();
    }

}
