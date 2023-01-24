package dev.hawu.plugins.api.gui.brushes;

import org.jetbrains.annotations.NotNull;

/**
 * The registry to hold all usable brushes for {@link dev.hawu.plugins.api.gui.GuiModel}.
 *
 * @since 1.7
 */
public final class BrushRegistry {

    private BrushRegistry() {
    }

    /**
     * Forms a layout brush.
     *
     * @return the layout brush
     * @since 1.7
     */
    @NotNull
    public static LayoutBrush.Builder layout() {
        return new LayoutBrush.Builder();
    }

    /**
     * Forms a border brush.
     *
     * @return the border brush
     * @since 2.0
     */
    @NotNull
    public static BorderBrush.Builder border() {
        return new BorderBrush.Builder();
    }

}
