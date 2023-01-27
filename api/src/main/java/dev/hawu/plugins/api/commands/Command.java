package dev.hawu.plugins.api.commands;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method as an invocable command.
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * The path or the full name of the command.
     * <p>
     * For example, if you want to capture when the user
     * runs {@code /foo bar baz <args>}, you should have
     * this argument be {@code foo bar baz} as args will be sliced
     * and returned to you via {@link dev.hawu.plugins.api.commands.CommandArgument}.
     *
     * @return the name of the command.
     * @since 2.0
     */
    @NotNull
    String value();

    /**
     * Specify the aliases of the command to register with.
     *
     * @return the aliases of the command.
     * @since 2.0
     */
    @NotNull
    String[] aliases() default {};

    /**
     * Types of sender that are eligible to run this command.
     *
     * @return said types.
     * @since 2.0
     */
    @NotNull
    SenderType[] senders() default {SenderType.PLAYER, SenderType.CONSOLE, SenderType.BLOCK, SenderType.PROXIED, SenderType.REMOTE_CONSOLE};

    /**
     * The message to send if the sender is ineligible to run this command.
     *
     * @return said message.
     * @since 2.0
     */
    @NotNull
    String message() default "";

}
