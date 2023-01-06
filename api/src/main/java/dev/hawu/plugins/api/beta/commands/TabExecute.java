package dev.hawu.plugins.api.beta.commands;

import dev.hawu.plugins.api.commands.SenderType;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that provides tab completions
 * for a command method in the same class.
 *
 * @since 2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TabExecute {

    /**
     * The path or the full name of the command to bind
     * this tab completer to.
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
     * Types of sender that are eligible to request tab completions of the command..
     *
     * @return said types.
     * @since 2.0
     */
    @NotNull
    SenderType[] senders() default {SenderType.PLAYER, SenderType.CONSOLE, SenderType.BLOCK, SenderType.PROXIED, SenderType.REMOTE_CONSOLE};

}
