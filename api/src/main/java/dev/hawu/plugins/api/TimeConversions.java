package dev.hawu.plugins.api;

import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Utility class for converting time durations
 * without taking the gregorian calendar into calculations.
 *
 * @since 1.0
 */
public final class TimeConversions {

    private static final Pattern separators = Pattern.compile("[\\s_,]");

    private TimeConversions() {}

    /**
     * Retrieves an instance of a time builder that gradually
     * adds different amounts of different units, then builds the milliseconds value.
     *
     * @return An instance of {@link TimeMillisBuilder}.
     * @since 1.0
     */
    @NotNull
    public static TimeMillisBuilder buildMillis() {
        return new TimeMillisBuilder();
    }

    /**
     * Retrieves an instance of a time builder that parses
     * a double value into a readable timestamp.
     *
     * @param value The value that needs parsing.
     * @return An instance of {@link TimeStringBuilder}.
     * @since 1.0
     */
    @NotNull
    public static TimeStringBuilder buildTimestamp(final double value) {
        return new TimeStringBuilder(value);
    }

    /**
     * Converts a {@link String} representation of duration of time
     * to a usable {@link Double} in milliseconds.
     *
     * @param value The value to convert.
     * @return The millis value of the {@link String}.
     * @since 1.0
     */
    public static double convertToMillis(@NotNull final String value) {
        StringBuilder digits = new StringBuilder();
        StringBuilder unit = new StringBuilder();
        double total = 0D;

        for(final char it : separators.matcher(value).replaceAll("").toCharArray()) {
            if(Character.isLetter(it)) {
                unit.append(it);
            } else {
                if(unit.length() > 0) {
                    final TimeUnit timeUnit = TimeUnit.fromName(unit.toString());
                    if(timeUnit != null) total += NumberUtils.toDouble(digits.toString()) * timeUnit.getMillisValue();

                    unit = new StringBuilder();
                    digits = new StringBuilder();
                }

                digits.append(it);
            }
        }

        if(unit.length() > 0) {
            final TimeUnit timeUnit = TimeUnit.fromName(unit.toString());
            if(timeUnit != null) total += NumberUtils.toDouble(digits.toString()) * timeUnit.getMillisValue();
        }

        return total;
    }

    /**
     * Converts a {@link Double} that represents the number of milliseconds
     * to a human-readable timestamp.
     *
     * @param value  The value to parse.
     * @param until  The smallest {@link TimeUnit} allowed.
     * @param spaces Whether to add spaces between units.
     * @param fully  Whether to use the units' full names instead of abbreviations.
     * @return The readable formatted {@link String} from value.
     * @since 1.0
     */
    @NotNull
    public static String convertToReadableFormat(final double value, @NotNull final TimeUnit until, final boolean spaces, final boolean fully) {
        double curr = value;
        final StringBuilder sb = new StringBuilder();
        for(final TimeUnit unit : TimeUnit.getReversedArray()) {
            if(unit.ordinal() < until.ordinal()) break;
            if(curr < unit.getMillisValue() || unit.shouldIgnore()) continue;

            final long quotient = Math.round(Math.floor(curr / unit.getMillisValue()));
            sb.append(quotient);

            if(fully && quotient > 1) sb.append(" ").append(unit.getPlural());
            else if(fully && quotient == 1) sb.append(" ").append(unit.getSingular());
            else sb.append(unit.getUnit());

            curr -= quotient * unit.getMillisValue();
            if(spaces) sb.append(" ");
        }

        return sb.toString().trim();
    }

    /**
     * Builder for building up milliseconds.
     *
     * @since 1.0
     */
    public static final class TimeMillisBuilder {

        private double total = 0D;

        private TimeMillisBuilder() {}

        /**
         * Adds to the total millis the value of the provided {@link String}
         * after parsing.
         *
         * @param s The value to add.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder fromString(@NotNull final String s) {
            total += convertToMillis(s);
            return this;
        }

        /**
         * Adds to the total millis a number of nanoseconds.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder nanoseconds(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.NANOSECOND.getMillisValue();
            return this;
        }

        /**
         * Adds to the total millis a number of microseconds.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder microseconds(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.MICROSECOND.getMillisValue();
            return this;
        }

        /**
         * Adds to the total millis a number of milliseconds.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder milliseconds(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.MILLISECOND.getMillisValue();
            return this;
        }

        /**
         * Adds to the total millis a number of seconds.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder seconds(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.SECOND.getMillisValue();
            return this;
        }

        /**
         * Adds to the total millis a number of minutes.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder minutes(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.MINUTE.getMillisValue();
            return this;
        }

        /**
         * Adds to the total millis a number of hours.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder hours(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.HOUR.getMillisValue();
            return this;
        }

        /**
         * Adds to the total millis a number of days.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder days(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.DAY.getMillisValue();
            return this;
        }

        /**
         * Adds to the total millis a number of weeks.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder weeks(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.WEEK.getMillisValue();
            return this;
        }

        /**
         * Adds to the total millis a number of months.
         *
         * @param amount The amount to append.
         * @return The same millis builder receiver.
         * @since 1.0
         */
        @NotNull
        public TimeMillisBuilder months(@NotNull final Number amount) {
            total += amount.doubleValue() * TimeUnit.MONTH.getMillisValue();
            return this;
        }

        /**
         * Constructs a {@link TimeStringBuilder} from the final amount
         * of milliseconds at invocation time.
         *
         * @return An instance of {@link TimeStringBuilder} from the total millis value.
         * @since 1.0
         */
        @NotNull
        public TimeStringBuilder toTimestampBuilder() {
            return new TimeStringBuilder(total);
        }

        /**
         * Retrieves the final amount of milliseconds at invocation time.
         *
         * @return The total milliseconds value.
         * @since 1.0
         */
        public double build() {
            return total;
        }

    }

    /**
     * Builder for building a readable timestamp.
     *
     * @since 1.0
     */
    public static final class TimeStringBuilder {

        private final double value;
        private TimeUnit unit = TimeUnit.MILLISECOND;
        private boolean spaces = false;
        private boolean fully = false;

        private TimeStringBuilder(final double value) {
            this.value = value;
        }

        /**
         * Sets the time unit that the conversions should stop at.
         *
         * @param unit The smallest unit to convert to.
         * @return The same time string builder.
         * @since 1.0
         */
        @NotNull
        public TimeStringBuilder until(@NotNull final TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        /**
         * Specifies that the timestamp needs spaces between each component.
         *
         * @return The same time string builder.
         * @since 1.0
         */
        @NotNull
        public TimeStringBuilder withSpaces() {
            this.spaces = true;
            return this;
        }

        /**
         * Specifies that the timestamp needs to append fully written
         * names of the time units, instead of their abbreviations.
         *
         * @return The same time string builder.
         * @since 1.0
         */
        @NotNull
        public TimeStringBuilder withNoAbbreviations() {
            this.fully = true;
            return this;
        }

        /**
         * Builds the {@link String} from the provided parameters.
         *
         * @return The formatted {@link String}.
         * @since 1.0
         */
        @NotNull
        public String build() {
            return TimeConversions.convertToReadableFormat(value, unit, spaces, fully);
        }

    }

}
