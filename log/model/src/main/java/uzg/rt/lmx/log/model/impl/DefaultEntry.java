package uzg.rt.lmx.log.model.impl;

import lombok.Data;
import uzg.rt.lmx.log.model.License;
import uzg.rt.lmx.log.model.Entry;
import uzg.rt.lmx.log.model.LogLevel;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class DefaultEntry implements Entry {
    private LocalDateTime timeStamp;
    private LogLevel level = LogLevel.NORMAL;
    private String message = "";
    private String errorMessage = "";
    private boolean checkIn = false;
    private boolean checkOut = false;
    private boolean status = false;
    private String username = "";
    private String hostname = "";
    private String ip = "";
    private Optional<License> license;

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public boolean isCheckOut() {
        return checkOut;
    }

    public boolean isStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public String getHostname() {
        return hostname;
    }

    public String getIp() {
        return ip;
    }

    public Optional<License> getLicense() {
        return license;
    }

    @Override
    public boolean isEmpty() {
        return timeStamp == null;
    }

}
