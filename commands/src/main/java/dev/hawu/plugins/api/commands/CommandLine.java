package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.collections.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A class that parses a list of arguments and sorts them
 * properly into a map of properties and list of leftover arguments.
 *
 * @since 1.0
 */
public final class CommandLine {

    private final @NotNull Set<@NotNull String> flags = new HashSet<>();
    private final @NotNull Set<@NotNull String> props = new HashSet<>();

    /**
     * Marks the following argument as a flag. Values that follow these
     * flags will never be considered a property.
     *
     * @param flag The flag to mark.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public CommandLine withFlag(@NotNull final String flag) {
        flags.add(flag);
        return this;
    }

    /**
     * Marks the following argument as an argument. If the provided argument is not a flag,
     * values following these will be considered a property.
     *
     * @param prop The property to mark.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public CommandLine withArgument(@NotNull final String prop) {
        props.add(prop);
        return this;
    }

    // Method to run the first iteration to take care of arguments within quotes
    // and those that are escaped by a preceding backslash.
    @NotNull
    private List<@NotNull String> parseQuotedArguments(@NotNull final String args) {
        boolean isRecording = false;
        final List<String> arguments = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int pointer = 0;

        while(pointer < args.length()) {
            if(isRecording) {
                if(args.charAt(pointer) == '\\' && pointer + 1 < args.length() && args.charAt(pointer + 1) == '"') {
                    current.append("\"");
                    pointer += 2;
                } else if(args.charAt(pointer) == '"') {
                    isRecording = false;
                    arguments.add(current.toString());
                    current = new StringBuilder();
                    pointer++;
                } else {
                    current.append(args.charAt(pointer));
                    pointer++;
                }
                continue;
            }

            if(args.charAt(pointer) == '"') isRecording = true;
            else if(args.charAt(pointer) == ' ') {
                if(current.length() > 0) {
                    arguments.add(current.toString());
                    current = new StringBuilder();
                }
            } else current.append(args.charAt(pointer));
            pointer++;
        }
        if(current.length() > 0) arguments.add(current.toString());

        return arguments;
    }

    @NotNull
    private Pair<@NotNull CommandArgument, @NotNull Map<@NotNull String, @NotNull List<@NotNull String>>> parseProperties(@NotNull final List<@NotNull String> arguments) {
        final Map<String, List<String>> properties = new HashMap<>();
        final List<String> leftovers = new ArrayList<>();
        int pointer = 0;

        while(pointer < arguments.size()) {
            if(arguments.get(pointer).startsWith("-")) {
                String value = null;
                int add = 1;

                if(props.contains(arguments.get(pointer)) && !flags.contains(arguments.get(pointer))
                        && pointer + 1 < arguments.size() && !arguments.get(pointer + 1).startsWith("-")) {
                    value = arguments.get(pointer + 1);
                    add = 2;
                }

                properties.putIfAbsent(arguments.get(pointer), new ArrayList<>());
                if(value != null) properties.get(arguments.get(pointer)).add(value);
                pointer += add;
            } else {
                leftovers.add(arguments.get(pointer));
                pointer++;
            }
        }

        return new Pair<>(new CommandArgument(leftovers), properties);
    }

    /**
     * Parse a {@link String} that represents the user's command input
     * and retrieves parsed properties and arguments.
     *
     * @param args The {@link String} representation of the arguments list.
     * @return A pair that holds a list of leftover arguments and a map of properties.
     * @since 1.0
     */
    @NotNull
    public Pair<@NotNull CommandArgument, @NotNull Map<@NotNull String, @NotNull List<@NotNull String>>> parse(@NotNull final String args) {
        return parseProperties(parseQuotedArguments(args));
    }

}
