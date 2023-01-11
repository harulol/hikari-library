package dev.hawu.plugins.api.gui.brushes;

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
    public static LayoutBrush.Builder newLayoutBrush() {
        return new LayoutBrush.Builder();
    }

}
