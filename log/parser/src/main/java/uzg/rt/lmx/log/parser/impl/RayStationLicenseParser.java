package uzg.rt.lmx.log.parser.impl;

import org.jetbrains.annotations.NotNull;
import uzg.rt.lmx.log.model.License;
import uzg.rt.lmx.log.model.impl.RayStationLicense;
import uzg.rt.lmx.log.parser.Parser;
import uzg.rt.lmx.log.parser.ParserException;

import java.util.regex.Pattern;

public class RayStationLicenseParser implements Parser<License> {
    @Override
    public License parse(@NotNull String s) throws ParserException {
        var license = new RayStationLicense();
        var regex = Pattern.compile("(.+)-(.+)-(.+)-(.+)");
        var matcher = regex.matcher(s);
        if (matcher.find()) {
            var versionParser = new VersionParser();
            license.setVersion(versionParser.parse(matcher.group(1).trim()));
            license.setType(matcher.group(2).trim());
            license.setName(matcher.group(3).trim());
            license.setHash(matcher.group(4).trim());
        }
        return license;
    }
}
