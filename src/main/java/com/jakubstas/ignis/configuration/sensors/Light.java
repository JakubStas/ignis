package com.jakubstas.ignis.configuration.sensors;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sensor.light")
public class Light {

    private int sunnyThreshold;

    public int getSunnyThreshold() {
        return sunnyThreshold;
    }

    public void setSunnyThreshold(int sunnyThreshold) {
        this.sunnyThreshold = sunnyThreshold;
    }
}
