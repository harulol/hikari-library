package dev.hawu.plugins.api.beta.commands;

import dev.hawu.plugins.api.commands.SenderType;
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
     * The name and aliases of the command.
     *
     * @return the name of the command.
     * @since 2.0
     */
    @NotNull
    String[] value();

    /**
     * Marks whether this method represents a child command
     * or a full command.
     *
     * @return if this method represents a child command.
     * @since 2.0
     */
    boolean child() default false;

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
    String message() default "&c&l(!) &cYou are not allowed to run this command.";

}
