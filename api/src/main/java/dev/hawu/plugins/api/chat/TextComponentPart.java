package dev.hawu.plugins.api.chat;

import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Represents a text component part that can be hovered
 * over or clicked on.
 *
 * @since 1.5
 */
public final class TextComponentPart {

    @Internal
    static class Content {

        private final String text;
        private final List<TextComponentPart> extras;

        Content(final String text, final List<TextComponentPart> extras) {
            this.text = text;
            this.extras = extras;
        }

    }

    @Internal
    static class Formatting {

        private final String color;
        private final String font;

        private final TextFormattingOption bold;
        private final TextFormattingOption italic;
        private final TextFormattingOption underlined;
        private final TextFormattingOption strikethrough;
        private final TextFormattingOption obfuscated;

        Formatting(final String color, final String font, final Map<String, TextFormattingOption> formattingOptionMap) {
            this.color = color;
            this.font = font;

            this.bold = formattingOptionMap.getOrDefault("bold", TextFormattingOption.INHERIT);
            this.italic = formattingOptionMap.getOrDefault("italic", TextFormattingOption.INHERIT);
            this.underlined = formattingOptionMap.getOrDefault("underlined", TextFormattingOption.INHERIT);
            this.strikethrough = formattingOptionMap.getOrDefault("strikethrough", TextFormattingOption.INHERIT);
            this.obfuscated = formattingOptionMap.getOrDefault("obfuscated", TextFormattingOption.INHERIT);
        }

    }

    @Internal
    static class Event {

        private final String insertion;
        private final TextClickEvent clickEvent;
        private final TextHoverEvent hoverEvent;

        Event(final String insertion, final TextClickEvent clickEvent, final TextHoverEvent hoverEvent) {
            this.insertion = insertion;
            this.clickEvent = clickEvent;
            this.hoverEvent = hoverEvent;
        }

    }

    private final Content content;
    private final Formatting formatting;
    private final Event event;

    /**
     * Creates a part of a chat component.
     *
     * @param content       the content of this part
     * @param event         the click, hover and insertion events
     * @param formatting    the formatting of this part
     * @since 1.5
     */
    public TextComponentPart(final @NotNull Content content, final @NotNull Formatting formatting, final @NotNull Event event) {
        this.content = content;
        this.formatting = formatting;
        this.event = event;
    }

    private void addFormatting(final String field, final TextFormattingOption option, final StringBuilder builder) {
        if(option != TextFormattingOption.INHERIT)
            builder.append(",\"").append(field).append("\":\"").append(option.name().toLowerCase()).append("\"");
    }

    private void addNotNull(final String field, final String value, final StringBuilder builder) {
        if(value != null)
            builder.append(",\"").append(field).append("\":\"").append(value).append("\"");
    }

    /**
     * Serializes this part into a JSON representation using
     * Minecraft's specification.
     *
     * @return Said string.
     * @since 1.5
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"text\":\"").append(StringEscapeUtils.escapeJava(content.text)).append("\"");

        addNotNull("color", formatting.color, sb);
        addFormatting("bold", formatting.bold, sb);
        addFormatting("italic", formatting.italic, sb);
        addFormatting("underlined", formatting.underlined, sb);
        addFormatting("strikethrough", formatting.strikethrough, sb);
        addFormatting("obfuscated", formatting.obfuscated, sb);
        addNotNull("font", formatting.font, sb);

        addNotNull("insertion", event.insertion, sb);
        if(event.clickEvent != null) sb.append(",").append(event.clickEvent);
        if(event.hoverEvent != null) sb.append(",").append(event.hoverEvent);

        if(content.extras.size() >= 1) {
            sb.append("\"extras\":[");
            final StringJoiner joiner = new StringJoiner(",");
            content.extras.stream().map(TextComponentPart::toString).forEach(joiner::add);
            sb.append(joiner).append("]");
        }

        return sb.append("}").toString();
    }

}
