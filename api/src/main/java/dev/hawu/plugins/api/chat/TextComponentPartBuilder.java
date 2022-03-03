package dev.hawu.plugins.api.chat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder to build {@link TextComponentPart}s.
 *
 * @since 1.5
 */
public final class TextComponentPartBuilder {

    private String text = "";
    private List<TextComponentPart> extras = new ArrayList<>();

    private String color = null;
    private String font = null;
    private TextFormattingOption bold = TextFormattingOption.INHERIT;
    private TextFormattingOption italic = TextFormattingOption.INHERIT;
    private TextFormattingOption underlined = TextFormattingOption.INHERIT;
    private TextFormattingOption strikethrough = TextFormattingOption.INHERIT;
    private TextFormattingOption obfuscated = TextFormattingOption.INHERIT;

    private String insertion = null;
    private TextClickEvent clickEvent = null;
    private TextHoverEvent hoverEvent = null;

    /**
     * Creates a new instance of the builder.
     *
     * @return Said instance.
     * @since 1.5
     */
    @NotNull
    public static TextComponentPartBuilder newBuilder() {
        return new TextComponentPartBuilder();
    }

    /**
     * Sets the text value of this part.
     *
     * @param text The value to set to.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setText(final @NotNull String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the extras value of this part.
     *
     * @param extras The extras part to set to.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setExtras(final @NotNull List<@NotNull TextComponentPart> extras) {
        this.extras = extras;
        return this;
    }

    /**
     * Sets the color for this part to be rendered in.
     *
     * @param color The color to render in.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setColor(final @Nullable String color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the font of this part.
     *
     * @param font The font to render in.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setFont(final @Nullable String font) {
        this.font = font;
        return this;
    }

    /**
     * Configures whether the text should be boldened.
     *
     * @param bold How the bold effect is decided.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setBold(final @NotNull TextFormattingOption bold) {
        this.bold = bold;
        return this;
    }

    /**
     * Configures whether the text should be italicized.
     *
     * @param italic How the italic effect is decided.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setItalic(final @NotNull TextFormattingOption italic) {
        this.italic = italic;
        return this;
    }

    /**
     * Configures whether the text should be underlined.
     *
     * @param underlined How the underlined effect is decided.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setUnderlined(final @NotNull TextFormattingOption underlined) {
        this.underlined = underlined;
        return this;
    }

    /**
     * Configures whether the text should be struck-through.
     *
     * @param strikethrough How the strikethrough effect is decided.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setStrikethrough(final @NotNull TextFormattingOption strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    /**
     * Configures whether the text should be obfuscated.
     *
     * @param obfuscated How the obfuscated effect is decided.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setObfuscated(final @NotNull TextFormattingOption obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    /**
     * Sets the text to insert into the player's chat-box on click.
     *
     * @param insertion The text to insert.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setInsertion(final @Nullable String insertion) {
        this.insertion = insertion;
        return this;
    }

    /**
     * Sets the handler to be called when the text is clicked.
     *
     * @param clickEvent The handler to call.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setClickEvent(final @Nullable TextClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    /**
     * Sets the handler to be called when the text is hovered over.
     *
     * @param hoverEvent The handler to call.
     * @return The same builder.
     * @since 1.5
     */
    public TextComponentPartBuilder setHoverEvent(final TextHoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        return this;
    }

    /**
     * Builds the part after being configured.
     *
     * @return The part.
     * @since 1.5
     */
    @NotNull
    public TextComponentPart build() {
        return new TextComponentPart(this.text, this.extras, this.color, this.font, this.bold,
            this.italic, this.underlined, this.strikethrough, this.obfuscated, this.insertion,
            this.clickEvent, this.hoverEvent);
    }

}
