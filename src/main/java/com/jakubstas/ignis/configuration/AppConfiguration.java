package com.jakubstas.ignis.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import com.jakubstas.ignis.configuration.sensors.Light;
import com.jakubstas.ignis.configuration.sensors.Moisture;
import com.jakubstas.ignis.configuration.sensors.Temperature;

@Configuration
@EnableConfigurationProperties({TwitterConfiguration.class, SensorsConfiguration.class, Moisture.class, Light.class, Temperature.class})
public class AppConfiguration {

    @Autowired
    private TwitterConfiguration twitterConfiguration;

    @Bean
    public Twitter twitter() {
        return new TwitterTemplate(twitterConfiguration.getAppId(), twitterConfiguration.getAppSecret(), twitterConfiguration.getAccessToken(),
                twitterConfiguration.getAccessTokenSecret());
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("personalities/personality");
        source.setUseCodeAsDefaultMessage(true);

        return source;
    }
}
