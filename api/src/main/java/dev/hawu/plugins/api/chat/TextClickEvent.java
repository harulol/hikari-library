package dev.hawu.plugins.api.chat;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an on-text-click event wrapper.
 *
 * @since 1.5
 */
public final class TextClickEvent {

    private final TextClickAction action;
    private final String value;

    /**
     * Constructs a simple text click event.
     *
     * @param action The action to perform.
     * @param value  The value to pass to the action.
     * @since 1.5
     */
    public TextClickEvent(final @NotNull TextClickAction action, final @NotNull String value) {
        this.action = action;
        this.value = value;
    }

    /**
     * Retrieves the JSON String representation of this event.
     *
     * @return Said string.
     * @since 1.5
     */
    @Override
    @NotNull
    public String toString() {
        return "\"clickEvent\":{\"action\":\"" + action.name().toLowerCase() + "\",\"value\":\"" + value + "\"}";
    }

}
