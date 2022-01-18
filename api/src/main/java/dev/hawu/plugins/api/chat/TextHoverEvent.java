package dev.hawu.plugins.api.chat;

import dev.hawu.plugins.api.reflect.MinecraftVersion;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the handler for the event
 * that the player hovers over a chat component.
 *
 * @since 1.5
 */
public final class TextHoverEvent {

    private final TextHoverAction action;
    private final String text;

    /**
     * Constructs an on-hover event handler.
     *
     * @param action The action to do.
     * @param value  The value of the event.
     * @since 1.5
     */
    public TextHoverEvent(final @NotNull TextHoverAction action, final @NotNull String value) {
        this.action = action;
        this.text = value;
    }

    /**
     * Retrieves the JSON representation as a string.
     *
     * @return Said string.
     * @since 1.5
     */
    @Override
    @NotNull
    public String toString() {
        if(MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_16_R1))
            return "\"hoverEvent\":{\"action\":\"" + action.name().toLowerCase() + "\",\"contents\":\"" + text + "\"}";
        else return "\"hoverEvent\":{\"action\":\"" + action.name().toLowerCase() + "\",\"value\":\"" + text + "\"}";
    }

}
