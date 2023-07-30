package dev.hawu.plugins.api.conversations;

import dev.hawu.plugins.api.collections.LazyReference;
import dev.hawu.plugins.api.collections.Property;
import org.bukkit.conversations.ConversationContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Wraps around a conversation context for smoother experience.
 */
public final class ConversationResolver {

    private final ConversationContext context;

    private LazyReference<String> promptText = LazyReference.of("");
    private Property<String> rawInput = Property.empty();
    private LazyReference<Integer> promptOffset = LazyReference.of(1);

    public ConversationResolver(ConversationContext context) {
        this.context = context;
    }

    /**
     * Retrieves the context for this resolver.
     *
     * @return The context.
     */
    @NotNull
    public ConversationContext getContext() {
        return context;
    }

    /**
     * Retrieves the prompt text for this exact prompt.
     *
     * @return The prompt text.
     */
    @NotNull
    public String getPromptText() {
        return promptText.get();
    }

    /**
     * Retrieves the raw input for this exact prompt. This input should always
     * be available.
     *
     * @return The raw input.
     */
    @NotNull
    public String getInput() {
        return rawInput.get();
    }

    /**
     * Retrieves the prompt offset for this exact prompt.
     *
     * @return The prompt offset.
     */
    public int getPromptOffset() {
        return promptOffset.get();
    }

    /**
     * Sets the prompt offset for this exact prompt.
     *
     * @param offset The offset to set.
     */
    public void setPromptOffset(int offset) {
        this.promptOffset = LazyReference.of(offset);
    }

    /**
     * Sets the raw input for this exact prompt.
     *
     * @param input The raw input to set.
     */
    public void setRawInput(@NotNull String input) {
        this.rawInput = Property.of(input);
    }

    /**
     * Supplies the context with the prompt text for this exact prompt.
     *
     * @param promptText The prompt text to supply.
     */
    public void supplyPromptText(@NotNull String promptText) {
        this.promptText = LazyReference.of(promptText);
    }

    /**
     * Supplies the context with the prompt text for this prompt using a function
     * to be computed later.
     *
     * @param func The prompt text to supply.
     */
    public void supplyPromptText(@NotNull Supplier<String> func) {
        this.promptText = LazyReference.of(func);
    }

}
