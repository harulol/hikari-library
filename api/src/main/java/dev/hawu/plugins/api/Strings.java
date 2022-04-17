package dev.hawu.plugins.api;

import dev.hawu.plugins.api.collections.tuples.Pair;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Utility class that provides operations on {@link String}s.
 *
 * @since 1.0
 */
public final class Strings {

    private static final DecimalFormat formatter = new DecimalFormat("#,###.##");

    private Strings() {}

    /**
     * Colorizes a {@link String} using Bukkit's provided
     * conversions.
     * <p>
     * This converts {@code &} to color symbols only if it precedes
     * a character that codes a color.
     *
     * @param s The string to convert.
     * @return {@code "null"} if {@code s} was null, a colorized {@link String} otherwise.
     * @since 1.0
     */
    @NotNull
    public static String color(@Nullable final String s) {
        if(s == null) return "null";
        else return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Attempts to convert a {@link String} representation
     * to an {@link UUID} via the native conversion.
     *
     * @param uuid The string to convert from.
     * @return A {@link UUID} if the conversion was successful, {@code null} otherwise.
     */
    @Nullable
    public static UUID toUUIDOrNull(@NotNull final String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch(final IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * Chops a string to smaller pieces of strings on each line.
     *
     * @param s         The string to chop.
     * @param maxLength The maximum length of each line.
     * @return A list of strings.
     * @since 1.6
     */
    @NotNull
    public static List<String> chop(final @NotNull String s, final int maxLength) {
        final String[] arr = s.split(" ");
        final List<String> result = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for(final String str : arr) {
            if(stringBuilder.length() + str.length() > maxLength - 1 && stringBuilder.length() > 0) {
                result.add(stringBuilder.toString().trim());
                stringBuilder = new StringBuilder(str);
                continue;
            }

            stringBuilder.append(" ").append(str);
        }

        if(stringBuilder.length() > 0) result.add(stringBuilder.toString());
        return result;
    }

    /**
     * Chops a list of strings to smaller pieces of strings on each line.
     *
     * @param original  The original string.
     * @param maxLength The maximum length of each line.
     * @return A list of strings.
     * @since 1.6
     */
    @NotNull
    public static List<String> chop(final @NotNull List<String> original, final int maxLength) {
        return chop(String.join(" ", original), maxLength);
    }

    /**
     * Checks if the provided {@link String} is equal to <strong>any</strong>
     * of the other strings.
     *
     * @param from    The string to compare with.
     * @param strings The strings to compare to.
     * @return True if {@code from} is equal to any of the {@code strings}.
     * @since 1.0
     */
    public static boolean equalsAny(@Nullable final String from, @Nullable final String @NotNull ... strings) {
        return Arrays.stream(strings).filter(Objects::nonNull).anyMatch(s -> s.equals(from));
    }

    /**
     * Checks if the provided {@link String} is equal to <strong>any</strong>
     * of the other strings.
     *
     * @param from    The string to compare with.
     * @param strings The strings to compare to.
     * @return True if {@code from} is equal to any of the {@code strings}.
     * @since 1.0
     */
    public static boolean equalsAnyIgnoresCase(@Nullable final String from, @Nullable final String @NotNull ... strings) {
        return Arrays.stream(strings).filter(Objects::nonNull).anyMatch(s -> s.equalsIgnoreCase(from));
    }

    /**
     * Formats a number and retrieves the {@link String} as the number representation
     * but with commas as digit separators and 2 fixed decimal places.
     *
     * @param number The number to format.
     * @return The formatted {@link String}.
     * @since 1.0
     */
    @NotNull
    public static String format(@NotNull final Number number) {
        return formatter.format(number);
    }

    /**
     * Fills in the placeholders if present within the {@link String} with the provided
     * pairs of parameters.
     * Parameters should be passed in as {@code Pair<String, Object>}s, with the first value
     * being non-null, and the second being nullable.
     * <p>
     * For a pair with {@code first=placeholder} and {@code second=value}, all occurrences of
     * {@code %placeholder%} with be replaced with {@code value}.
     *
     * @param message The message to fill.
     * @param params  The parameters to fill with.
     * @return The formatted {@link String}.
     * @since 1.0
     */
    @NotNull
    public static String fillPlaceholders(@NotNull final String message, @NotNull final Pair<?, ?> @NotNull ... params) {
        String curr = message;
        for(Pair<?, ?> param : params) {
            if(param.getFirstOption().orElse(null) == null) continue;
            curr = curr.replace("%" + param.getFirst() + "%", param.getSecondOption().orElse(null) == null ? "null" : param.getSecond().toString());
        }
        return color(curr);
    }

}
