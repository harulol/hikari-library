package dev.hawu.plugins.api.commands;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method, if it is an invocable command that
 * the sender needs permissions to run.
 * <p>
 * If the method does not have {@link Command}, this annotation is useless.
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandPermission {

    /**
     * The permissions required to run this command.
     * Blank if no permissions necessary.
     *
     * @return said permissions.
     * @since 2.0
     */
    @NotNull
    String[] value() default {};

    /**
     * The message to send to the sender if they do not have
     * the required permissions.
     *
     * @return said message.
     * @since 2.0
     */
    @NotNull
    String message() default "&c&l(!) &cYou do not have permission(s) to run this command.";

}
