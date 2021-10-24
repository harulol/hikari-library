package dev.hawu.plugins.api.inventories;

/**
 * Enumerated values representing how many slots a double
 * chest inventory can have for GUIs.
 *
 * @since 1.0
 */
public enum InventorySize {

    /**
     * Represents a chest inventory with one
     * row, or 9 slots.
     *
     * @since 1.0
     */
    ONE_ROW,

    /**
     * Represents a chest inventory with two
     * rows, or 18 slots.
     *
     * @since 1.0
     */
    TWO_ROWS,

    /**
     * Represents a chest inventory with three
     * rows, or 27 slots.
     *
     * @since 1.0
     */
    THREE_ROWS,

    /**
     * Represents a chest inventory with four
     * rows, or 36 slots.
     *
     * @since 1.0
     */
    FOUR_ROWS,

    /**
     * Represents a chest inventory with five rows,
     * or 45 slots.
     *
     * @since 1.0
     */
    FIVE_ROWS,

    /**
     * Represents a chest inventory with six rows,
     * or 54 slots.
     *
     * @since 1.0
     */
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
