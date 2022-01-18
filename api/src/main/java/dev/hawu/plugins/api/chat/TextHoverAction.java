package dev.hawu.plugins.api.chat;

/**
 * The action to do when the player hovers
 * over an embedded component.
 *
 * @since 1.5
 */
public enum TextHoverAction {

    /**
     * Shows a piece of text.
     *
     * @since 1.5
     */
    SHOW_TEXT,

    /**
     * Shows information about
     * an entity (similar to death messages).
     *
     * @since 1.5
     */
    SHOW_ENTITY,

    /**
     * Shows information about an item stack
     * (similar to hovering over one in inventories).
     *
     * @since 1.5
     */
    SHOW_ITEM

}
