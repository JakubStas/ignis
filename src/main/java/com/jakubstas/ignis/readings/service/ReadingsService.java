package com.jakubstas.ignis.readings.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jakubstas.ignis.readings.model.Readings;
import com.jakubstas.ignis.social.TwitterService;

@Service
public class ReadingsService {

    private final Logger logger = LoggerFactory.getLogger(ReadingsService.class);

    @Autowired
    private TwitterService twitterService;

    public void processReadings(final Readings readings) {
        logger.info(readings.getAsTweet());

        twitterService.postTweet(readings.getAsTweet());
    }
}
