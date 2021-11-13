package dev.hawu.plugins.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * All types of measurable and supported time units
 * for duration conversions.
 *
 * @since 1.0
 */
public enum TimeUnit {

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 0.000001} milliseconds.
     *
     * @since 1.0
     */
    NANOSECOND(1e-6, "ns"),

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 0.001} milliseconds.
     *
     * @since 1.0
     */
    MICROSECOND(1e-3, "μs"),

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 1} millisecond.
     *
     * @since 1.0
     */
    MILLISECOND(1.0, "ms"),

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 1000} milliseconds.
     *
     * @since 1.0
     */
    SECOND(1e3, "s"),

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 60,000} milliseconds.
     *
     * @since 1.0
     */
    MINUTE(6e4, "m"),

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 3,600,000} milliseconds.
     *
     * @since 1.0
     */
    HOUR(3.6e6, "h"),

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 86,400,000} milliseconds.
     *
     * @since 1.0
     */
    DAY(8.64e7, "d"),

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 604,800,000} milliseconds.
     *
     * @since 1.0
     */
    WEEK(6.048e8, "w"),

    /**
     * Represents to a type of unit of time measurement
     * that evaluates to {@code 2,628,000,000} milliseconds.
     *
     * @since 1.0
     */
    MONTH(2.628e9, "mo");

    private static final Set<TimeUnit> ignored = new HashSet<>(Collections.singletonList(WEEK));
    private static TimeUnit[] reversedArray;

    private final double millisValue;
    private final String unit;
    private final String singular;
    private final String plural;

    TimeUnit(final double millisValue, @NotNull final String unit) {
        this.millisValue = millisValue;
        this.unit = unit;
        this.singular = this.name().toLowerCase();
        this.plural = this.name().toLowerCase() + "s";
    }

    /**
     * Collects this enum class's values but as reversed.
     *
     * @return The reversed array of the units.
     * @since 1.0
     */
    @NotNull
    public static TimeUnit @NotNull [] getReversedArray() {
        if(reversedArray != null) return reversedArray;

        final TimeUnit[] values = values();
        final TimeUnit[] reversed = new TimeUnit[values.length];

        for(int i = 0; i < values.length; i++) {
            reversed[values.length - i - 1] = values[i];
        }

        return reversedArray = reversed;
    }

    /**
     * Retrieves a {@link TimeUnit} instance from the name or the abbreviation
     * of it.
     *
     * @param name The name to match cases with.
     * @return The {@link TimeUnit} if found, {@code null} otherwise.
     * @since 1.0
     */
    @Nullable
    public static TimeUnit fromName(@NotNull final String name) {
        switch(name) {
            case "ns":
            case "nano":
            case "nanos":
            case "nanosecond":
            case "nanoseconds":
                return NANOSECOND;
            case "mms":
            case "μs":
            case "micro":
            case "micros":
            case "microsecond":
            case "microseconds":
                return MICROSECOND;
            case "ms":
            case "milli":
            case "millis":
            case "millisecond":
            case "milliseconds":
                return MILLISECOND;
            case "s":
            case "second":
            case "seconds":
                return SECOND;
            case "m":
            case "minute":
            case "minutes":
                return MINUTE;
            case "h":
            case "hour":
            case "hours":
                return HOUR;
            case "d":
            case "day":
            case "days":
                return DAY;
            case "w":
            case "week":
            case "weeks":
                return WEEK;
            case "mo":
            case "month":
            case "months":
                return MONTH;
            default:
                return null;
        }
    }

    /**
     * Retrieves the value of the unit in milliseconds.
     *
     * @return The millis value of this unit.
     * @since 1.0
     */
    public final double getMillisValue() {
        return millisValue;
    }

    /**
     * Retrieves the abbreviation of the name for the unit.
     *
     * @return The unit's name abbreviation.
     * @since 1.0
     */
    @NotNull
    public final String getUnit() {
        return unit;
    }

    /**
     * Retrieves the full singular form of the name for this unit.
     *
     * @return The singular form of the name.
     * @since 1.0
     */
    @NotNull
    public final String getSingular() {
        return singular;
    }

    /**
     * Retrieves the full plural form of the name for this unit.
     *
     * @return The plural form of the name.
     * @since 1.0
     */
    @NotNull
    public final String getPlural() {
        return plural;
    }

    /**
     * Checks if this is a unit that should be excluded
     * from conversions to human-readable time.
     *
     * @return Whether it should be ignored.
     * @since 1.2
     */
    public final boolean shouldIgnore() {
        return ignored.contains(this);
    }

}
