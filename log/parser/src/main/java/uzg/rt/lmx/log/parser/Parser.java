package uzg.rt.lmx.log.parser;

import org.jetbrains.annotations.NotNull;

public interface Parser<T> {
    T parse(@NotNull String s) throws ParserException;
}
