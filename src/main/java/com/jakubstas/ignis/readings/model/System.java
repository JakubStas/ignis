package com.jakubstas.ignis.readings.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class System {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss, EEE dd.MM.yyyy");

    private final ZoneId zoneId = ZoneId.of("Europe/Dublin");

    private String status;

    private String timestamp;

    public System() {
        this.status = "running";
        this.timestamp = dateTimeFormatter.format(LocalDateTime.now(zoneId));
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "System{" +
                ", status='" + status + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}