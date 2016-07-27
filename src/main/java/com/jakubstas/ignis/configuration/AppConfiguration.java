package com.jakubstas.ignis.configuration;

import com.jakubstas.ignis.configuration.sensors.Light;
import com.jakubstas.ignis.configuration.sensors.Moisture;
import com.jakubstas.ignis.configuration.sensors.Temperature;
import com.jakubstas.ignis.wemo.WemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
@EnableConfigurationProperties({TwitterConfiguration.class, SensorsConfiguration.class, Moisture.class, Light.class, Temperature.class, WemoConfiguration.class})
public class AppConfiguration {

    @Autowired
    private TwitterConfiguration twitterConfiguration;

    @Autowired
    private WemoConfiguration wemoConfiguration;

    private final String wemoUrlTemplate = "http://%s:%d/upnp/control/basicevent1";

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

    @Bean
    public Jaxb2Marshaller marshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.jakubstas.ignis.wemo.wsdl");

        return marshaller;
    }

    @Bean
    public WemoService wemoService(Jaxb2Marshaller marshaller) {
        final String uri = String.format(wemoUrlTemplate, wemoConfiguration.getIpAddress(), wemoConfiguration.getPort());

        final WemoService client = new WemoService();
        client.setDefaultUri(uri);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);

        return client;
    }
}