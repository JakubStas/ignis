package com.jakubstas.ignis.configuration.sensors;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sensor.moisture")
public class Moisture {

    private int wateringThreshold;

    public int getWateringThreshold() {
        return wateringThreshold;
    }

    public void setWateringThreshold(int wateringThreshold) {
        this.wateringThreshold = wateringThreshold;
    }
}
