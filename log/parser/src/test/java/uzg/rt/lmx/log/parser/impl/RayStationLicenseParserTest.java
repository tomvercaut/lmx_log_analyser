package uzg.rt.lmx.log.parser.impl;

import org.junit.jupiter.api.Test;
import uzg.rt.lmx.log.model.License;
import uzg.rt.lmx.log.model.Version;
import uzg.rt.lmx.log.model.impl.RayStationLicense;
import uzg.rt.lmx.log.parser.ParserException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayStationLicenseParserTest {

    @Test
    void parse() throws ParserException {
        final var inputs = List.of(
                "9_2_0-Clinical-rayAnatomy-f692546qfasdf4q7a7a6210ad2",
                "9_1_8-Clinical-rayPlatform-79cae7lkjf23496yf9ac3423iu5ecda85e"
        );

        var lic0 = new RayStationLicense();
        lic0.setVersion(new Version(9,2,0));
        lic0.setType("Clinical");
        lic0.setName("rayAnatomy");
        lic0.setHash("f692546qfasdf4q7a7a6210ad2");
        var lic1 = new RayStationLicense();
        lic1.setVersion(new Version(9,1,8));
        lic1.setType("Clinical");
        lic1.setName("rayPlatform");
        lic1.setHash("79cae7lkjf23496yf9ac3423iu5ecda85e");

        final var expected = List.of(
                lic0, lic1
        );

        List<License> licenses = new ArrayList<>();
        var parser = new RayStationLicenseParser();
        for (var input : inputs) {
            licenses.add(parser.parse(input));
        }
        assertEquals(expected, licenses);
    }
}