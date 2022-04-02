package uzg.rt.lmx.log.parser.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uzg.rt.lmx.log.model.License;
import uzg.rt.lmx.log.model.LogLevel;
import uzg.rt.lmx.log.model.Version;
import uzg.rt.lmx.log.model.impl.RayStationLicense;
import uzg.rt.lmx.log.parser.ParserException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DefaultLmxEntryParserTest {
    private final List<String> lines = new ArrayList<>();

    @BeforeEach
    void setUp() {
        lines.add("[2022-03-14 10:57:59] CHECKOUT by user@server [192.168.1.5]: 9_2_0-Clinical-rayStationDoctorBase-i34871kcqaerio02138g");
        lines.add("[2022-03-11 12:29:22]");
        lines.add("[2022-03-11 12:29:22]");
        lines.add("[2022-03-11 12:29:22] License server using TCP IPv4 port 6200.");
        lines.add("[2022-03-11 13:27:11] WARNING: Automatic server discovery might not work properly.");
        lines.add("[2022-03-11 15:27:07] CHECKOUT by user@server [::1]: 9_2_0-Clinical-rayStationPlanningBase-4b79kph1fd98fye375qak5e");
        lines.add("[2022-03-11 15:28:01] CHECKIN by user@server [::1]: 9_2_0-Clinical-rayPlatform-91ai62klk610jadfubh1");
        lines.add("[2022-03-11 15:40:10] STATUS by user@server [192.168.1.5]");
        lines.add("[2022-03-14 09:57:31] CHECKOUT by user@server [::1]: 9_2_0-Clinical-rayPlatform-184kadbt1yg401habca FAIL: No more licenses available");
    }

    @AfterEach
    void tearDown() {
        lines.clear();
    }


    @Test
    void getTimeStamp() throws ParserException {
        final List<LocalDateTime> expected = new ArrayList<>();
        expected.add(LocalDateTime.of(2022, 3, 14, 10, 57, 59, 0));
        expected.add(LocalDateTime.of(2022, 3, 11, 12, 29, 22, 0));
        expected.add(LocalDateTime.of(2022, 3, 11, 12, 29, 22, 0));
        expected.add(LocalDateTime.of(2022, 3, 11, 12, 29, 22, 0));
        expected.add(LocalDateTime.of(2022, 3, 11, 13, 27, 11, 0));
        expected.add(LocalDateTime.of(2022, 3, 11, 15, 27, 7, 0));
        expected.add(LocalDateTime.of(2022, 3, 11, 15, 28, 1, 0));
        expected.add(LocalDateTime.of(2022, 3, 11, 15, 40, 10, 0));
        expected.add(LocalDateTime.of(2022, 3, 14, 9, 57, 31, 0));
        final List<LocalDateTime> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.getTimeStamp());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void getLevel() throws ParserException {
        final List<LogLevel> expected = new ArrayList<>();
        expected.add(LogLevel.NORMAL);
        expected.add(LogLevel.NORMAL);
        expected.add(LogLevel.NORMAL);
        expected.add(LogLevel.NORMAL);
        expected.add(LogLevel.WARNING);
        expected.add(LogLevel.NORMAL);
        expected.add(LogLevel.NORMAL);
        expected.add(LogLevel.NORMAL);
        expected.add(LogLevel.FAIL);
        final List<LogLevel> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.getLevel());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void getErrorMessage() throws ParserException {
        final List<String> expected = new ArrayList<>();
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("Automatic server discovery might not work properly.");
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("No more licenses available");
        final List<String> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.getErrorMessage());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void isCheckIn() throws ParserException {
        final List<Boolean> expected = new ArrayList<>();
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        final List<Boolean> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.isCheckIn());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void isCheckOut() throws ParserException {
        final List<Boolean> expected = new ArrayList<>();
        expected.add(true);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        final List<Boolean> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.isCheckOut());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void isStatus() throws ParserException {
        final List<Boolean> expected = new ArrayList<>();
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(true);
        expected.add(false);
        final List<Boolean> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.isStatus());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void isEmpty() throws ParserException {
        final List<Boolean> expected = new ArrayList<>();
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        expected.add(false);
        final List<Boolean> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.isEmpty());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void getUsername() throws ParserException {
        final List<String> expected = new ArrayList<>();
        expected.add("user");
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("user");
        expected.add("user");
        expected.add("user");
        expected.add("user");
        final List<String> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.getUsername());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void getHostname() throws ParserException {
        final List<String> expected = new ArrayList<>();
        expected.add("server");
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("server");
        expected.add("server");
        expected.add("server");
        expected.add("server");
        final List<String> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.getHostname());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void getIp() throws ParserException {
        final List<String> expected = new ArrayList<>();
        expected.add("192.168.1.5");
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("");
        expected.add("::1");
        expected.add("::1");
        expected.add("192.168.1.5");
        expected.add("::1");
        final List<String> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.getIp());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }

    @Test
    void getMessage() throws ParserException {
        final List<String> expected = new ArrayList<>();
        expected.add("CHECKOUT by user@server [192.168.1.5]: 9_2_0-Clinical-rayStationDoctorBase-i34871kcqaerio02138g");
        expected.add("");
        expected.add("");
        expected.add("License server using TCP IPv4 port 6200.");
        expected.add("WARNING: Automatic server discovery might not work properly.");
        expected.add("CHECKOUT by user@server [::1]: 9_2_0-Clinical-rayStationPlanningBase-4b79kph1fd98fye375qak5e");
        expected.add("CHECKIN by user@server [::1]: 9_2_0-Clinical-rayPlatform-91ai62klk610jadfubh1");
        expected.add("STATUS by user@server [192.168.1.5]");
        expected.add("CHECKOUT by user@server [::1]: 9_2_0-Clinical-rayPlatform-184kadbt1yg401habca FAIL: No more licenses available");
        final List<String> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.getMessage());
        }
        assertEquals(expected.size(), entries.size());
        assertEquals(expected, entries);
    }


    @Test
    void getLicense() throws ParserException {
        final List<Optional<License>> expected = new ArrayList<>();
        expected.add(Optional.of(new RayStationLicense(new Version(9, 2, 0), "Clinical", "rayStationDoctorBase", "i34871kcqaerio02138g")));
        expected.add(Optional.empty());
        expected.add(Optional.empty());
        expected.add(Optional.empty());
        expected.add(Optional.empty());
        expected.add(Optional.of(new RayStationLicense(new Version(9, 2, 0), "Clinical", "rayStationPlanningBase", "4b79kph1fd98fye375qak5e")));
        expected.add(Optional.of(new RayStationLicense(new Version(9, 2, 0), "Clinical", "rayPlatform", "91ai62klk610jadfubh1")));
        expected.add(Optional.empty());
        expected.add(Optional.of(new RayStationLicense(new Version(9, 2, 0), "Clinical", "rayPlatform", "184kadbt1yg401habca")));
        final List<Optional<License>> entries = new ArrayList<>();
        final var parser = new DefaultLmxLogEntryParser();
        for (var line : lines) {
            final var entry = parser.parse(line);
            entries.add(entry.getLicense());
        }
        assertEquals(expected.size(), entries.size());
        // assertEquals(expected, entries);
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), entries.get(i));
        }
    }


}