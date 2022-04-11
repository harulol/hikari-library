package dev.hawu.plugins.api.dsl.items

import dev.hawu.plugins.api.dsl.ScopeControlMarker
import dev.hawu.plugins.api.dsl.misc.color

/**
 * The specification for controlling an item's lore.
 */
@ScopeControlMarker
class ItemLoreSpec internal constructor(private val lore: MutableList<String>) {

    /**
     * Clears the lore.
     */
    val clear: Unit
        get() {
            lore.clear()
        }

    /**
     * Adds a line to the lore.
     */
    operator fun String.unaryPlus() {
        lore.add(this.color())
    }

    /**
     * Adds lines to the lore.
     */
    operator fun Array<String>.unaryPlus() {
        lore.addAll(this.map { it.color() })
    }

    /**
     * Adds lines to the lore.
     */
    operator fun Collection<String>.unaryPlus() {
        lore.addAll(this.map { it.color() })
    }

    /**
     * Replaces all text that matches the placeholders.
     */
    fun replace(vararg params: Pair<String, Any?>) {
        for(i in 0 until lore.size) {
            params.forEach { (k, v) ->
                lore[i] = lore[i].replace("%$k%", v.toString())
            }
            lore[i] = lore[i].color()
        }
    }

}
