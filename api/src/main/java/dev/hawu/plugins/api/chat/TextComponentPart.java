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

        if(color != null) sb.append(",\"color\":\"").append(color).append("\"");

        if(bold == TextFormattingOption.TRUE) sb.append(",\"bold\":\"true\"");
        else if(bold == TextFormattingOption.FALSE) sb.append(",\"bold\":\"false\"");

        if(italic == TextFormattingOption.TRUE) sb.append(",\"italic\":\"true\"");
        else if(italic == TextFormattingOption.FALSE) sb.append(",\"italic\":\"false\"");

        if(underlined == TextFormattingOption.TRUE) sb.append(",\"underlined\":\"true\"");
        else if(underlined == TextFormattingOption.FALSE) sb.append(",\"underlined\":\"false\"");

        if(strikethrough == TextFormattingOption.TRUE) sb.append(",\"strikethrough\":\"true\"");
        else if(strikethrough == TextFormattingOption.FALSE) sb.append(",\"strikethrough\":\"false\"");

        if(obfuscated == TextFormattingOption.TRUE) sb.append(",\"obfuscated\":\"true\"");
        else if(obfuscated == TextFormattingOption.FALSE) sb.append(",\"obfuscated\":\"false\"");

        if(font != null) sb.append(",\"font\":\"").append(font).append("\"");
        if(insertion != null) sb.append(",\"insertion\":\"").append(insertion).append("\"");
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
