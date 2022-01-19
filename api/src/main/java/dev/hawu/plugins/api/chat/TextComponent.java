package dev.hawu.plugins.api.chat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * Represents a list of text component parts
 * that make up the full and complete chat message.
 *
 * @since 1.5
 */
public final class TextComponent {

    private final List<TextComponentPart> list;

    /**
     * Constructs an empty text component.
     *
     * @since 1.5
     */
    public TextComponent() {
        this.list = new ArrayList<>();
    }

    /**
     * Constructs a text component with already built parts.
     *
     * @param list The list of parts.
     * @since 1.5
     */
    public TextComponent(final @NotNull List<@NotNull TextComponentPart> list) {
        this.list = list;
    }

    /**
     * Adds a component part to the list.
     *
     * @param part The part to add.
     * @since 1.5
     */
    public void add(final @NotNull TextComponentPart part) {
        this.list.add(part);
    }

    /**
     * Adds a text component part to the list.
     *
     * @param builder The consumer to configure the builder.
     * @since 1.5
     */
    public void add(final @NotNull Consumer<TextComponentPartBuilder> builder) {
        final TextComponentPartBuilder partBuilder = new TextComponentPartBuilder();
        builder.accept(partBuilder);
        this.list.add(partBuilder.build());
    }

    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(",", list.isEmpty() ? "[\"\"" : "[\"\",", "]");
        list.stream().map(TextComponentPart::toString).forEach(joiner::add);
        return joiner.toString();
    }

}
