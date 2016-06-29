package com.jakubstas.ignis.configuration;

import com.jakubstas.ignis.configuration.sensors.Moisture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sensor")
public class SensorsConfiguration {

    @Autowired
    private Moisture moisture;

    public Moisture getMoisture() {
        return moisture;
    }
}
