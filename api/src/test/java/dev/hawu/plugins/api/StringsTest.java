package dev.hawu.plugins.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class StringsTest {

    @Test
    public void chopStringTest() {
        // Should be split to:
        // Hello,
        // world!
        // This is a
        // test.
        final List<String> chopped = Strings.chop("Hello, world! This is a test.", 10);

        Assertions.assertEquals(chopped.size(), 4);
        Assertions.assertEquals(chopped.get(0), "Hello,");
        Assertions.assertEquals(chopped.get(1), "world!");
        Assertions.assertEquals(chopped.get(2), "This is a");
        Assertions.assertEquals(chopped.get(3), "test.");
    }

}
