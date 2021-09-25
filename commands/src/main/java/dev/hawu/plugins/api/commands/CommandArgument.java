package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.collections.tuples.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

/**
 * A wrapper class that represents the arguments that come with
 * every command executions.
 * @since 1.0
 */
@SuppressWarnings("deprecation")
public final class CommandArgument {

    private final List<String> args;
    private CommandLine cli;

    /**
     * Initialize the class with the arguments in a form of a {@link String} array.
     * @param strings The strings to initialize with.
     * @since 1.0
     */
    public CommandArgument(@NotNull final String[] strings) {
        this.args = Arrays.asList(strings);
    }

    /**
     * Initialize the class with the arguments in the already converted {@link List}.
     * @param strings The strings to initialize with.
     * @since 1.0
     */
    public CommandArgument(@NotNull final List<String> strings) {
        this.args = strings;
    }

    /**
     * Initialize the property {@link CommandArgument#cli} so {@link CommandArgument#parse()} can be
     * used later.
     * @param cli The CLI instance to initialize with.
     * @return The same receiver.
     * @since 1.0
     */
    public CommandArgument withCLI(@NotNull final CommandLine cli) {
        this.cli = cli;
        return this;
    }

    /**
     * Attempts to retrieve a name of a {@link Player} from the provided position.
     * @param index The index to retrieve at.
     * @return A {@link Player} if index is not out of bounds and the name is of a valid player, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public Player getPlayer(final int index) {
        return index >= args.size() ? null : Bukkit.getPlayer(args.get(index));
    }

    /**
     * Attempts to retrieve a name of an {@link OfflinePlayer} from the provided position.
     * @param index The index to retrieve at.
     * @return A {@link OfflinePlayer} if index is not out of bounds, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public OfflinePlayer getOfflinePlayer(final int index) {
        return index >= args.size() ? null : Bukkit.getOfflinePlayer(args.get(index));
    }

    /**
     * Gets the argument at the position.
     * @param index The position to retrieve.
     * @return The argument at the position, or {@code null} if index is out of bounds.
     * @since 1.0
     */
    @Nullable
    public String get(final int index) {
        return index >= args.size() ? null : args.get(index);
    }

    /**
     * Retrieves the size of the underlying list.
     * @return The size of the arguments.
     * @since 1.0
     */
    public int size() {
        return args.size();
    }

    /**
     * Retrieves the underlying list of this argument.
     * @return The underlying list.
     * @since 1.0
     */
    public List<String> getUnderlyingList() {
        return args;
    }

    /**
     * Retrieves the argument at the position.
     * @param index The position to retrieve at.
     * @return The argument at the position.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     * @since 1.0
     */
    @NotNull
    public String getNonNull(final int index) {
        return args.get(index);
    }

    /**
     * Parses this argument into properties and leftovers.
     * @return The pair that holds leftover arguments and a map of properties.
     * @throws NullPointerException If the property CLI has not been initialized.
     * @since 1.0
     */
    @NotNull
    public Pair<CommandArgument, Map<String, List<String>>> parse() {
        return Objects.requireNonNull(cli, "CLI property is not initialized.").parse(joinToString(0, args.size()));
    }

    /**
     * Joins a number of arguments into a {@link String}, delimited by spaces.
     * @param startInclusive The index to start joining, inclusive.
     * @param endExclusive The index to stop joining, exclusive.
     * @return The joined {@link String} result.
     * @since 1.0
     */
    @NotNull
    public String joinToString(final int startInclusive, final int endExclusive) {
        final StringJoiner joiner = new StringJoiner(" ");
        IntStream.range(startInclusive, endExclusive).forEach(i -> joiner.add(args.get(i)));
        return joiner.toString();
    }

    /**
     * Slices the arguments into a {@link String[]} between a range.
     * @param startInclusive The index to start slicing, inclusive.
     * @param endExclusive The index to stop slicing, exclusive.
     * @return The sliced {@link String[]}.
     * @since 1.0
     */
    @NotNull
    public String[] sliceArray(final int startInclusive, final int endExclusive) {
        final String[] array = new String[endExclusive - startInclusive];
        IntStream.range(startInclusive, endExclusive).forEach(i -> array[i] = args.get(i));
        return array;
    }

    /**
     * Slices the arguments into a List of String between a range.
     * @param startInclusive The index to start slicing, inclusive.
     * @param endExclusive The index to stop slicing, exclusive.
     * @return The sliced {@link List} of {@link String}.
     * @since 1.0
     */
    @NotNull
    public List<String> slice(final int startInclusive, final int endExclusive) {
        final List<String> list = new ArrayList<>();
        IntStream.range(startInclusive, endExclusive).forEach(i -> list.add(args.get(i)));
        return list;
    }

}
