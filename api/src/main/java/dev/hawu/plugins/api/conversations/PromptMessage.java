package dev.hawu.plugins.api.conversations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotated with {@link PromptMessage} has to be a method that takes
 * 1 parameter of {@link ConversationResolver} and returns {@code void}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@PromptHandler
public @interface PromptMessage {
}
