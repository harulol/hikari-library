package dev.hawu.plugins.api.dsl.items

import dev.hawu.plugins.api.dsl.ScopeControlMarker
import org.bukkit.inventory.ItemFlag

/**
 * Controls an item stack's item flags.
 */
@ScopeControlMarker
class ItemFlagsSpec internal constructor(private val flags: MutableSet<ItemFlag>) {

    /**
     * No flags should be present.
     */
    val none: Unit
        get() {
            flags.clear()
        }

    /**
     * Adds HIDE_ATTRIBUTES.
     */
    val hideAttributes: Unit
        get() {
            flags.add(ItemFlag.HIDE_ATTRIBUTES)
        }

    /**
     * Adds HIDE_ENCHANTS.
     */
    val hideEnchantments: Unit
        get() {
            flags.add(ItemFlag.HIDE_ENCHANTS)
        }

    /**
     * Adds HIDE_POTION_EFFECTS.
     */
    val hidePotionEffects: Unit
        get() {
            flags.add(ItemFlag.HIDE_POTION_EFFECTS)
        }

    /**
     * Adds HIDE_DESTROYS.
     */
    val hideDestroys: Unit
        get() {
            flags.add(ItemFlag.HIDE_DESTROYS)
        }

    /**
     * Adds HIDE_UNBREAKABLE.
     */
    val hideUnbreakable: Unit
        get() {
            flags.add(ItemFlag.HIDE_UNBREAKABLE)
        }

    /**
     * Adds HIDE_PLACED_ON
     */
    val hidePlacedOn: Unit
        get() {
            flags.add(ItemFlag.HIDE_PLACED_ON)
        }

    /**
     * Adds every flag.
     */
    val hideAll: Unit
        get() {
            flags.addAll(ItemFlag.values())
        }

    /**
     * Adds a flag.
     */
    operator fun ItemFlag.unaryPlus() {
        flags.add(this)
    }

    /**
     * Removes a flag.
     */
    operator fun ItemFlag.unaryMinus() {
        flags.remove(this)
    }

}
