package dev.hawu.plugins.api.dsl.gui.brushes

import dev.hawu.plugins.api.collections.MutableProperty
import dev.hawu.plugins.api.gui.GuiModel
import dev.hawu.plugins.api.gui.brushes.LayoutBrush

/**
 * Specification for a LayoutBrush.
 *
 * @since 1.7
 */
class LayoutBrushSpec internal constructor() {

    /**
     * The model as the backing canvas for this brush.
     */
    val model = MutableProperty.empty<GuiModel>()
    private var layout: Array<out String> = arrayOf()

    /**
     * Sets the layout of this layout brush
     * for dropping items.
     *
     * @param str the lines of the layout
     * @since 1.7
     */
    fun layout(vararg str: String) {
        this.layout = str
    }

    internal fun build(): LayoutBrush {
        return LayoutBrush(model.get(), *layout)
    }

}
