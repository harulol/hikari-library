package dev.hawu.plugins.api.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandArgumentTest {

    @Test
    @DisplayName("Array slicing test")
    public void sliceArray() {
        final CommandArgument args = new CommandArgument(new String[]{"hello", "there"});
        Assertions.assertDoesNotThrow(() -> {
            args.slice(1, args.size());
        });
        Assertions.assertDoesNotThrow(() -> {
            args.sliceArray(1, args.size());
        });
    }

}
