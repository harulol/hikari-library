package dev.hawu.plugins.api.dsl.items

import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.BannerMeta
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.FireworkEffectMeta
import org.bukkit.inventory.meta.FireworkMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta

/**
 * Specify the item meta for the item stacks specification.
 */
@ScopeControlMarker
class ItemMetaSpec internal constructor(private val material: Material) {

    private var transformer: ItemMeta.() -> Unit = {}

    /**
     * The display name of the item, can be null for reasons.
     */
    val name = MutableProperty.of<String>(null).nullable()
    val lore = MutableProperty.of(mutableListOf<String>())
    val flags = MutableProperty.of(mutableSetOf<ItemFlag>())
    val enchantments = MutableProperty.of(mutableMapOf<Enchantment, Int>())

    /**
     * Opens a lore specification.
     */
    fun lore(spec: ItemLoreSpec.() -> Unit) {
        ItemLoreSpec(lore.get()).spec()
    }

    /**
     * Opens an item flag specification.
     */
    fun flags(spec: ItemFlagsSpec.() -> Unit) {
        ItemFlagsSpec(flags.get()).spec()
    }

    /**
     * Copy all the values from [another][other] item meta.
     */
    fun copy(other: ItemMeta) {
        name.set(other.displayName)
        lore.set(other.lore)
        flags.set(other.itemFlags)
        enchantments.set(other.enchants)
    }

    /**
     * Transforms the item meta with the given [transformer].
     */
    fun transform(transformer: ItemMeta.() -> Unit) {
        this.transformer = {
            this.transformer()
            transformer()
        }
    }

    /**
     * Transforms the item meta with the given [transformer]
     * as another meta type.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ItemMeta> transformed(transformer: T.() -> Unit) {
        transform {
            (this as T).transformer()
        }
    }

    /**
     * Transforms the item meta as a [BannerMeta].
     */
    fun banner(transformer: BannerMeta.() -> Unit) {
        transformed<BannerMeta> {
            transformer()
        }
    }

    /**
     * Transforms the item meta as a [BookMeta].
     */
    fun book(transformer: BookMeta.() -> Unit) {
        transformed<BookMeta> {
            transformer()
        }
    }

    /**
     * Transforms the item meta as a [EnchantmentStorageMeta].
     */
    fun enchantmentStorage(transformer: EnchantmentStorageMeta.() -> Unit) {
        transformed<EnchantmentStorageMeta> {
            transformer()
        }
    }

    /**
     * Transforms the item meta as a [FireworkEffectMeta].
     */
    fun fireworkEffect(transformer: FireworkEffectMeta.() -> Unit) {
        transformed<FireworkEffectMeta> {
            transformer()
        }
    }

    /**
     * Transforms the item meta as a [FireworkMeta].
     */
    fun firework(transformer: FireworkMeta.() -> Unit) {
        transformed<FireworkMeta> {
            transformer()
        }
    }

    /**
     * Transforms the item meta as a [LeatherArmorMeta].
     */
    fun leatherArmor(transformer: LeatherArmorMeta.() -> Unit) {
        transformed<LeatherArmorMeta> {
            transformer()
        }
    }

    /**
     * Transforms the item meta as a [MapMeta].
     */
    fun map(transformer: MapMeta.() -> Unit) {
        transformed<MapMeta> {
            transformer()
        }
    }

    /**
     * Transforms the item meta as a [PotionMeta].
     */
    fun potion(transformer: PotionMeta.() -> Unit) {
        transformed<PotionMeta> {
            transformer()
        }
    }

    /**
     * Transforms the item meta as a [SkullMeta].
     */
    fun skull(transformer: SkullMeta.() -> Unit) {
        transformed<SkullMeta> {
            transformer()
        }
    }

    internal fun build(): ItemMeta {
        val meta = Bukkit.getItemFactory().getItemMeta(material)
        meta.displayName = name.get()
        meta.lore = lore.get()
        meta.addItemFlags(*flags.get().toTypedArray())
        enchantments.get().forEach { (k, v) -> meta.addEnchant(k, v, true) }
        return meta
    }

}
