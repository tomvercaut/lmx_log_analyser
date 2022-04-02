package uzg.rt.lmx.log.io;

import org.jetbrains.annotations.NotNull;
import uzg.rt.lmx.log.model.Log;
import uzg.rt.lmx.log.parser.ParserException;
import uzg.rt.lmx.log.parser.impl.DefaultLmxLogEntryParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogReader {

    public static Log read(@NotNull Path path) throws IOException, ParserException {
        final var lines = Files.readAllLines(path);
        final var parser = new DefaultLmxLogEntryParser();
        final var log = new Log();
        for (String line : lines) {
            final var entry = parser.parse(line);
            if (entry != null) {
                log.add(entry);
            }
        }
        return log;
    }
}
