package com.jakubstas.ignis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IgnisConfiguration {

    @Autowired
    private TwitterConfiguration twitterConfiguration;

    @Autowired
    private SensorsConfiguration sensorsConfiguration;

    public TwitterConfiguration getTwitterConfiguration() {
        return twitterConfiguration;
    }

    public SensorsConfiguration getSensorsConfiguration() {
        return sensorsConfiguration;
    }
}
