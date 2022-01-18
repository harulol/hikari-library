package dev.hawu.plugins.api.chat;

/**
 * The action to happen when the player clicks
 * on the embedded component.
 *
 * @since 1.5
 */
public enum TextClickAction {

    /**
     * Runs a command as the player.
     *
     * @since 1.5
     */
    RUN_COMMAND,

    /**
     * Opens a URL in the player's web browser.
     *
     * @since 1.5
     */
    OPEN_URL,

    /**
     * Copies a piece of text to the player's clipboard.
     *
     * @since 1.5
     */
    COPY_TO_CLIPBOARD,

    /**
     * Suggest a command in the player's text box.
     *
     * @since 1.5
     */
    SUGGEST_COMMAND,

    /**
     * Change the page of the player's book.
     *
     * @since 1.5
     */
    CHANGE_PAGE

}
