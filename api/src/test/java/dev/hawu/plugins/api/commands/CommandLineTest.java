package dev.hawu.plugins.api.commands;

import dev.hawu.plugins.api.collections.tuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class CommandLineTest {

    @Test
    @DisplayName("Parser Test")
    public void parserTest() {
        final String args = "test -s \"S flag\" -m -f -g hello";
        final Pair<CommandArgument, Map<String, List<String>>> pair = new CommandLine().withFlag("-f").withFlag("-m").withArgument("-s").parse(args);

        Assertions.assertEquals(2, pair.getFirst().size());
        Assertions.assertTrue(pair.getSecond().get("-m").isEmpty(), "Properties map: " + pair.getSecond());
        Assertions.assertTrue(pair.getSecond().get("-g").isEmpty(), "Properties map: " + pair.getSecond());
    }

}
