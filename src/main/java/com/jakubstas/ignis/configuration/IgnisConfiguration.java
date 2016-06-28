package com.jakubstas.ignis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IgnisConfiguration {

    @Autowired
    private TwitterConfiguration twitterConfiguration;

    public TwitterConfiguration getTwitterConfiguration() {
        return twitterConfiguration;
    }
}
