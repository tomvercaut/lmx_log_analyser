package uzg.rt.lmx.log.parser.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uzg.rt.lmx.log.model.Version;
import uzg.rt.lmx.log.parser.ParserException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VersionParserTest {

    @Test
    void parse() throws ParserException {
        final var inputs = List.of("9_2_0", "12_13_14");
        final var expected = List.of(
                new Version(9,2,0),
                new Version(12,13,14)
        );
        List<Version> versions = new ArrayList<>();
        var parser = new VersionParser();
        for (var input : inputs) {
            versions.add(parser.parse(input));
        }
        assertEquals(expected, versions);
    }

    @Test
    void parseExpectedException() {
        var parser = new VersionParser();
        ParserException thrown = Assertions.assertThrows(ParserException.class,
                () -> {
            parser.parse("9.1.5");
                }, "ParserException was expected");
        String msg = thrown.getMessage();
        assertTrue(msg.startsWith("Invalid string format"));
    }
}