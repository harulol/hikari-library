package dev.hawu.plugins.api.inventories.item;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.collections.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the lore of an item stack with a builder
 * pattern for easier configurations.
 *
 * @since 1.0
 */
public final class LoreBuilder {

    private final List<String> lore;
    private final ItemMetaBuilder hook;

    LoreBuilder(@NotNull final ItemMetaBuilder caller) {
        this.lore = caller.getLore();
        this.hook = caller;
    }

    /**
     * Clears the entire lore list.
     *
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public LoreBuilder clear() {
        lore.clear();
        return this;
    }

    /**
     * Appends an array of lines to the lore, all of them will be colorized
     * using {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)}.
     *
     * @param lines The lines to append.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public LoreBuilder add(@NotNull final String... lines) {
        lore.addAll(Arrays.stream(lines).map(Strings::color).collect(Collectors.toList()));
        return this;
    }

    /**
     * Appends an array of lines to a specific position in the lore. All
     * the lines will be colorized using {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)}.
     *
     * @param index The index to append at.
     * @param lines The lines to append.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public LoreBuilder add(final int index, @NotNull final String... lines) {
        lore.addAll(index, Arrays.stream(lines).map(Strings::color).collect(Collectors.toList()));
        return this;
    }

    /**
     * Appends a collection of lines at the end of the lore.
     * All lines will be colorized with the alternate color code {@code &}.
     *
     * @param collection The collection to append.
     * @return The same receiver.
     * @since 1.0
     */
    public LoreBuilder addAll(final @NotNull Collection<? extends String> collection) {
        lore.addAll(collection.stream().map(Strings::color).collect(Collectors.toList()));
        return this;
    }

    /**
     * Appends a collection of lines at a specific position in the lore.
     * All lines will be colorized with the alternate color code {@code &}.
     *
     * @param index      The index to append at.
     * @param collection The collection to append.
     * @return The same receiver.
     * @since 1.0
     */
    public LoreBuilder addAll(final int index, @NotNull final Collection<? extends String> collection) {
        lore.addAll(index, collection.stream().map(Strings::color).collect(Collectors.toList()));
        return this;
    }

    /**
     * Overrides the value of the line at a specific position in the lore.
     * The new line will be colorized using {@link org.bukkit.ChatColor#translateAlternateColorCodes(char, String)}.
     *
     * @param index The position to override at.
     * @param line  The line to override with.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public LoreBuilder set(final int index, @NotNull final String line) {
        lore.set(index, Strings.color(line));
        return this;
    }

    /**
     * Loops through each line of the lore, then replaces all occurrences with their values
     * from the provided pairs of parameters.
     * <p>
     * With a pair of ("key", "value"), this will replace
     * all occurrences of {@code %key%} to {@code value}.
     *
     * @param pairs The pairs holding values what to replace with.
     * @return The same receiver.
     * @since 1.0
     */
    @NotNull
    public LoreBuilder loopAndFill(final @NotNull Pair<?, ?>... pairs) {
        for(int i = 0; i < lore.size(); i++) {
            String value = lore.get(i);
            for(final Pair<?, ?> pair : pairs) {
                value = value.replace("%" + pair.getFirst() + "%", pair.getSecond() == null ? "null" : pair.getSecond().toString());
            }
            lore.set(i, Strings.color(value));
        }
        return this;
    }

    /**
     * Builds the lore from the provided configurations and retrieves
     * the builder for the meta.
     *
     * @return The hooked meta builder.
     * @since 1.0
     */
    @NotNull
    public ItemMetaBuilder build() {
        return hook.withLore(this.lore);
    }

}
