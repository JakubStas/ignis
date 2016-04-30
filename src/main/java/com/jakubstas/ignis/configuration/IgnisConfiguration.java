package com.jakubstas.ignis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
@EnableConfigurationProperties({TwitterConfiguration.class})
public class IgnisConfiguration {

    @Autowired
    private TwitterConfiguration twitterConfiguration;

    @Bean
    public Twitter twitter() {
        return new TwitterTemplate(twitterConfiguration.getAppId(), twitterConfiguration.getAppSecret(), twitterConfiguration.getAccessToken(), twitterConfiguration.getAccessTokenSecret());
    }
}
