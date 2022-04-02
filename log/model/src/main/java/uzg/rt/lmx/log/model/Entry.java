package uzg.rt.lmx.log.model;

import java.time.LocalDateTime;
import java.util.Optional;

public interface Entry {
    LocalDateTime getTimeStamp();
    LogLevel getLevel();
    String getMessage();

    String getErrorMessage();
    boolean isCheckIn();
    boolean isCheckOut();
    boolean isStatus();
    boolean isEmpty();
    String getUsername();
    String getHostname();
    String getIp();
    Optional<License> getLicense();
}
