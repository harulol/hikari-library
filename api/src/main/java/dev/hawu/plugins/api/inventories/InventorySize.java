package dev.hawu.plugins.api.inventories;

/**
 * Enumerated values representing how many slots a double
 * chest inventory can have for GUIs.
 *
 * @since 1.0
 */
public enum InventorySize {

    ONE_ROW,
    TWO_ROWS,
    THREE_ROWS,
    FOUR_ROWS,
    FIVE_ROWS,
    SIX_ROWS;

    /**
     * The amount of slots from the enum.
     *
     * @return The number of slots.
     * @since 1.0
     */
    public final int getSlots() {
        return (this.ordinal() + 1) * 9;
    }

}
