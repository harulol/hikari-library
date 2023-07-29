package dev.hawu.plugins.api.conversations;

import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.ValidatingPrompt;

/**
 * Represents the type of a prompt.
 */
public enum PromptType {

    MESSAGE_PROMPT(MessagePrompt.class),
    VALIDATING_PROMPT(ValidatingPrompt.class),
    BOOLEAN_PROMPT(BooleanPrompt.class),
    NUMERIC_PROMPT(NumericPrompt.class);

    private final Class<?> clazz;

    PromptType(final Class<?> cls) {
        this.clazz = cls;
    }

}
