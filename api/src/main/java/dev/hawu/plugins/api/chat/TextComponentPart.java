package dev.hawu.plugins.api.chat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.StringJoiner;

/**
 * Represents a text component part that can be hovered
 * over or clicked on.
 *
 * @since 1.5
 */
public final class TextComponentPart {

    private final String text;
    private final List<TextComponentPart> extras;

    private final String color;
    private final String font;
    private final TextFormattingOption bold;
    private final TextFormattingOption italic;
    private final TextFormattingOption underlined;
    private final TextFormattingOption strikethrough;
    private final TextFormattingOption obfuscated;

    private final String insertion;
    private final TextClickEvent clickEvent;
    private final TextHoverEvent hoverEvent;

    /**
     * Creates a part of a chat component.
     *
     * @param text          The content of this part.
     * @param extras        The children of this part.
     * @param color         The color to render the content in.
     * @param font          The font to render the content in.
     * @param bold          Whether to render this part as bold.
     * @param italic        Whether to render this part as italic.
     * @param underlined    Whether to render this part as underlined.
     * @param strikethrough Whether to render this part as strikethrough.
     * @param obfuscated    Whether to render this part as obfuscated.
     * @param insertion     The text to insert into the chat box when clicked.
     * @param clickEvent    The event to trigger when this part is clicked.
     * @param hoverEvent    The event to trigger when this part is hovered over.
     * @since 1.5
     */
    public TextComponentPart(final @NotNull String text, final @NotNull List<@NotNull TextComponentPart> extras,
                             final @Nullable String color, final @Nullable String font, final @NotNull TextFormattingOption bold,
                             final @NotNull TextFormattingOption italic, final @NotNull TextFormattingOption underlined, final @NotNull TextFormattingOption strikethrough,
                             final @NotNull TextFormattingOption obfuscated, final @Nullable String insertion,
                             final @Nullable TextClickEvent clickEvent, final @Nullable TextHoverEvent hoverEvent) {
        this.text = text;
        this.extras = extras;
        this.color = color;
        this.font = font;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.insertion = insertion;
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
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
        sb.append("{\"text\":\"").append(text).append("\"");

        addNotNull("color", color, sb);
        addFormatting("bold", bold, sb);
        addFormatting("italic", italic, sb);
        addFormatting("underlined", underlined, sb);
        addFormatting("strikethrough", strikethrough, sb);
        addFormatting("obfuscated", obfuscated, sb);

        addNotNull("font", font, sb);
        addNotNull("insertion", insertion, sb);
        if(clickEvent != null) sb.append(",").append(clickEvent);
        if(hoverEvent != null) sb.append(",").append(hoverEvent);
        if(extras.size() >= 1) {
            sb.append("\"extras\":[");
            final StringJoiner joiner = new StringJoiner(",");
            extras.stream().map(TextComponentPart::toString).forEach(joiner::add);
            sb.append(joiner).append("]");
        }

        return sb.append("}").toString();
    }

}
