package com.jakubstas.ignis.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class IgnisConfiguration {

    private final Logger logger = LoggerFactory.getLogger(IgnisConfiguration.class);

    @Autowired
    private TwitterConfiguration twitterConfiguration;

    @Autowired
    private SensorsConfiguration sensorsConfiguration;

    @Autowired
    private WemoConfiguration wemoConfiguration;

    @Autowired
    private PubNubConfiguration pubNubConfiguration;

    @PostConstruct
    public void init() {
        logger.info(twitterConfiguration.toString());
        logger.info(sensorsConfiguration.toString());
        logger.info(wemoConfiguration.toString());
        logger.info(pubNubConfiguration.toString());
    }

    public TwitterConfiguration getTwitterConfiguration() {
        return twitterConfiguration;
    }

    public SensorsConfiguration getSensorsConfiguration() {
        return sensorsConfiguration;
    }

    public WemoConfiguration getWemoConfiguration() {
        return wemoConfiguration;
    }

    public PubNubConfiguration getPubNubConfiguration() {
        return pubNubConfiguration;
    }
}
