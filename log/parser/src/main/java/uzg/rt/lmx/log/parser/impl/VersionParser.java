package uzg.rt.lmx.log.parser.impl;

import org.jetbrains.annotations.NotNull;
import uzg.rt.lmx.log.model.Version;
import uzg.rt.lmx.log.parser.Parser;
import uzg.rt.lmx.log.parser.ParserException;

import java.util.regex.Pattern;

public class VersionParser implements Parser<Version> {
    @Override
    public Version parse(@NotNull String s) throws ParserException {
        var v = new Version();
        final var regex = Pattern.compile("(.+)_(.+)_(.+)");
        final var matcher = regex.matcher(s);
        if (matcher.find()) {
            v.setMajor(matcher.group(1).trim());
            v.setMinor(matcher.group(2).trim());
            v.setPatch(matcher.group(3).trim());
            return v;
        }
        throw new ParserException(String.format("Invalid string format: %s.", s));
    }
}
