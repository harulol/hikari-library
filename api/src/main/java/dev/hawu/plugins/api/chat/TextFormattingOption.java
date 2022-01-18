package dev.hawu.plugins.api.chat;

/**
 * Represents the option for formatting text
 * in chat components.
 *
 * @since 1.5
 */
public enum TextFormattingOption {

    /**
     * Uses whatever the value is from the parent component.
     *
     * @since 1.5
     */
    INHERIT,

    /**
     * Enables this formatting option regardless of parent's choice.
     *
     * @since 1.5
     */
    TRUE,

    /**
     * Disables this formatting option regardless of parent's choice.
     *
     * @since 1.5
     */
    FALSE

}
