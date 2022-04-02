package uzg.rt.lmx.log.parser.impl;

import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import uzg.rt.lmx.log.model.License;
import uzg.rt.lmx.log.model.LogLevel;
import uzg.rt.lmx.log.model.impl.DefaultEntry;
import uzg.rt.lmx.log.parser.Parser;
import uzg.rt.lmx.log.parser.ParserException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

public class DefaultLmxLogEntryParser implements Parser<DefaultEntry> {

    private static final String PATTERN_DATE_TIME_MESSAGE =
            "^\\[(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})\\](.+)?$";
    private static final Pattern REGEX_DATE_TIME_MESSAGE = Pattern.compile(PATTERN_DATE_TIME_MESSAGE);

    private static final String PATTERN_CHECKOUT = "^.*CHECKOUT.*$";
    private static final Pattern REGEX_CHECKOUT = Pattern.compile(PATTERN_CHECKOUT);

    private static final String PATTERN_CHECKIN = "^.*CHECKIN.*$";
    private static final Pattern REGEX_CHECKIN = Pattern.compile(PATTERN_CHECKIN);

    private static final String PATTERN_STATUS = "^.*STATUS.*$";
    private static final Pattern REGEX_STATUS = Pattern.compile(PATTERN_STATUS);

    private static final String PATTERN_USERNAME_SERVER_IP = "^.*\\s(\\S+)@(\\S+)\\s\\[(.+)\\].*$";
    private static final Pattern REGEX_USERNAME_SERVER_IP = Pattern.compile(PATTERN_USERNAME_SERVER_IP);

    private static final String PATTERN_LICENSE = "^.*\\]:\\s(\\S+)(\\s.*)?$";
    private static final Pattern REGEX_LICENSE = Pattern.compile(PATTERN_LICENSE);

    private static final String PATTERN_WARNING = "^.*WARNING:\\s(.*)$";
    private static final Pattern REGEX_WARNING = Pattern.compile(PATTERN_WARNING);

    private static final String PATTERN_FAIL = "^.*FAIL:\\s(.*)$";
    private static final Pattern REGEX_FAIL = Pattern.compile(PATTERN_FAIL);


    @Override
    public DefaultEntry parse(@NotNull String s) throws ParserException {
        var entry = new DefaultEntry();
        {
            var opt = getTimeStampAndMessage(s);
            if (opt.isEmpty()) {
                return entry;
            }
            {
                final var dateTimeStampAndMessage = opt.get();
                final var timeStamp = dateTimeStampAndMessage.getKey();
                entry.setTimeStamp(timeStamp);
                entry.setMessage(dateTimeStampAndMessage.getValue().trim());
            }
        }
        {
            final var levelAndErrMsgPair = getLogLevelAndMessage(entry.getMessage());
            entry.setLevel(levelAndErrMsgPair.getKey());
            entry.setErrorMessage(levelAndErrMsgPair.getValue());
        }
        entry.setCheckOut(isCheckOut(entry.getMessage()));
        entry.setCheckIn(isCheckIn(entry.getMessage()));
        entry.setStatus(isStatus(entry.getMessage()));
        {
            var opt = getUserServerIp(entry.getMessage());
            if (opt.isPresent()) {
                final var triplet = opt.get();
                entry.setUsername(triplet.getLeft());
                entry.setHostname(triplet.getMiddle());
                entry.setIp(triplet.getRight());
            }
        }
        {
            var license = getLicense(entry.getMessage());
            entry.setLicense(license);
        }
        return entry;
    }

    private Optional<Pair<LocalDateTime, String>> getTimeStampAndMessage(@NotNull String s) {
        var matcher = REGEX_DATE_TIME_MESSAGE.matcher(s);
        if (matcher.find()) {
            var year = Integer.parseInt(matcher.group(1));
            var month = Integer.parseInt(matcher.group(2));
            var day = Integer.parseInt(matcher.group(3));
            var hour = Integer.parseInt(matcher.group(4));
            var min = Integer.parseInt(matcher.group(5));
            var sec = Integer.parseInt(matcher.group(6));
            var msg = matcher.group(7) == null ? "" : matcher.group(7).trim();
            var dateTime = LocalDateTime.of(year, month, day, hour, min, sec, 0);
            return Optional.of(Pair.of(dateTime, msg));
        }
        return Optional.empty();
    }

    private Pair<LogLevel, String> getLogLevelAndMessage(@NotNull String s) {
        var matcher = REGEX_WARNING.matcher(s);
        if (matcher.find()) {
            final var errMsg = matcher.group(1);
            return Pair.of(LogLevel.WARNING, errMsg);
        }
        matcher = REGEX_FAIL.matcher(s);
        if (matcher.find()) {
            final var errMsg = matcher.group(1);
            return Pair.of(LogLevel.FAIL, errMsg);
        }
        return Pair.of(LogLevel.NORMAL, "");
    }

    private boolean isCheckOut(@NotNull String s) {
        final var matcher = REGEX_CHECKOUT.matcher(s);
        return matcher.find();
    }

    private boolean isCheckIn(@NotNull String s) {
        final var matcher = REGEX_CHECKIN.matcher(s);
        return matcher.find();
    }

    private boolean isStatus(@NotNull String s) {
        final var matcher = REGEX_STATUS.matcher(s);
        return matcher.find();
    }

    private Optional<Triple<String, String, String>> getUserServerIp(@NotNull String s) {
        final var matcher = REGEX_USERNAME_SERVER_IP.matcher(s);
        if (matcher.find()) {
            final var username = Optional.ofNullable(matcher.group(1)).orElse("");
            final var server = Optional.ofNullable(matcher.group(2)).orElse("");
            final var ip = Optional.ofNullable(matcher.group(3)).orElse("");
            return Optional.of(Triple.of(username, server, ip));
        }
        return Optional.empty();
    }

    private Optional<License> getLicense(@NotNull String s) {
        final var matcher = REGEX_LICENSE.matcher(s);
        if (matcher.find()) {
            final var t = matcher.group(1);
            if (t.contains("-ray")) {
                final var parser = new RayStationLicenseParser();
                try {
                    final var license = parser.parse(t);
                    return Optional.of(license);
                } catch (ParserException e) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}
