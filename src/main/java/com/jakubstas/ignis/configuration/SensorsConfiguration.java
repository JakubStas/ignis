package com.jakubstas.ignis.configuration;

import com.jakubstas.ignis.configuration.sensors.Light;
import com.jakubstas.ignis.configuration.sensors.Moisture;
import com.jakubstas.ignis.configuration.sensors.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sensor")
public class SensorsConfiguration {

    @Autowired
    private Moisture moisture;

    @Autowired
    private Light light;

    @Autowired
    private Temperature temperature;

    public Moisture getMoisture() {
        return moisture;
    }

    public Light getLight() {
        return light;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "SensorsConfiguration{" +
                "moisture=" + moisture +
                ", light=" + light +
                ", temperature=" + temperature +
                '}';
    }
}
