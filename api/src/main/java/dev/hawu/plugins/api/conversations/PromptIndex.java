package dev.hawu.plugins.api.conversations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates the prompt to be at position and where it leads if successful.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PromptIndex {

    /**
     * The index of the prompt.
     *
     * @return The index of the prompt.
     */
    int value();

    /**
     * The type of the prompt.
     *
     * @return The type of the prompt.
     */
    PromptType type() default PromptType.MESSAGE_PROMPT;

}
