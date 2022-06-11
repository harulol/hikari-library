package dev.hawu.plugins.api.dsl.gui

import dev.hawu.plugins.api.dsl.gui.brushes.LayoutBrushSpec
import dev.hawu.plugins.api.gui.brushes.LayoutBrush

/**
 * Creates a new layout brush for the given model.
 */
fun useLayoutBrush(spec: LayoutBrushSpec.() -> Unit): LayoutBrush {
    return LayoutBrushSpec().apply(spec).build()
}
