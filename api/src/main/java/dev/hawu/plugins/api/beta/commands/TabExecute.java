package dev.hawu.plugins.api.beta.commands;

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

}
