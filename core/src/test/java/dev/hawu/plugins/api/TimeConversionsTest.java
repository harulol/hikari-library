package dev.hawu.plugins.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class TimeConversionsTest {

    @Test
    @DisplayName("Test Conversion to Double")
    void doubleConversion() {
        final double indirect = TimeConversions.convertToMillis("1h3m2m23s");
        final double direct = TimeConversions.buildMillis()
                .hours(1)
                .minutes(3)
                .minutes(2)
                .seconds(23)
                .build();

        Assertions.assertEquals(indirect, direct);
    }

    @Test
    @DisplayName("Test Conversion to Double with decimals")
    void doubleDecimalsConversion() {
        final double value = TimeConversions.convertToMillis("0.5h");
        final double expected = TimeConversions.convertToMillis("30m");

        Assertions.assertEquals(value, expected);
    }

    @Test
    @DisplayName("Test Conversion to String")
    void stringConversion() {
        Assertions.assertEquals("1h 5m 23s", TimeConversions.buildTimestamp(3923000).until(TimeUnit.SECOND).withSpaces().build());
    }

    @Test
    @DisplayName("Test Conversion to full String")
    void fullStringConversion() {
        Assertions.assertEquals("1 hour 5 minutes 23 seconds", TimeConversions.buildTimestamp(3923000)
                .until(TimeUnit.SECOND).withSpaces().withNoAbbreviations().build());
    }

}
