package dev.hawu.plugins.api.dsl.items

import dev.hawu.plugins.api.dsl.potions.PotionEffectSpec
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

/**
 * Creates an item stack from specifications.
 */
fun itemStack(material: Material = Material.AIR, spec: ItemStackSpec.() -> Unit): ItemStack {
    val specification = ItemStackSpec(material)
    return specification.apply(spec).build()
}

/**
 * Creates an item stack from a base and specifications.
 */
fun itemStack(from: ItemStack = ItemStack(Material.AIR), spec: ItemStackSpec.() -> Unit): ItemStack {
    val specification = ItemStackSpec(from)
    return specification.apply(spec).build()
}

/**
 * Creates a potion effect from specifications.
 */
fun potionEffect(spec: PotionEffectSpec.() -> Unit): PotionEffect = PotionEffectSpec().apply(spec).build()
