package dev.hawu.plugins.api.dsl.items

import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.dsl.nbt.NBTCompoundSpec
import dev.hawu.plugins.api.nbt.NBTCompound
import dev.hawu.plugins.api.nbt.NBTRegistry
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Specification for creating an item stack.
 */
@ScopeControlMarker
class ItemStackSpec internal constructor(from: ItemStack) {

    val material = MutableProperty.of(Material.AIR)
    val amount = MutableProperty.of(1)
    val durability = MutableProperty.of<Short>(0)
    val compound = MutableProperty.of<NBTCompound>(null).nullable()

    private var meta: ItemMeta? = null

    init {
        material.set(from.type)
        amount.set(from.amount)
        durability.set(from.durability)
        meta = from.itemMeta
        compound.set(NBTRegistry.getRegistry().getCompound(from))
    }

    internal constructor(material: Material) : this(ItemStack(material))

    /**
     * Configures this item stack's meta.
     */
    fun meta(spec: ItemMetaSpec.() -> Unit) {
        meta = ItemMetaSpec(material.get()).apply(spec).build()
    }

    /**
     * Opens a NBTCompound specification to configure.
     */
    fun compound(spec: NBTCompoundSpec.() -> Unit) {
        if(compound.isPresent) NBTCompoundSpec(compound.get()).apply(spec)
        else compound.set(NBTCompoundSpec().apply(spec).build())
    }

    internal fun build(): ItemStack {
        val stack = ItemStack(material.get(), amount.get(), durability.get())
        if(meta != null) stack.itemMeta = meta
        val stackCompound = NBTRegistry.getRegistry().getCompound(stack) ?: NBTCompound()
        if(compound.isPresent) stackCompound.putAll(compound.get())
        return NBTRegistry.getRegistry().applyCompound(stack, stackCompound)
    }

}
