package com.jakubstas.ignis.personality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import static com.jakubstas.ignis.personality.Personality.DEFAULT;

@Component
public class PersonalityMessageSource {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(final String key, final Object... args) {
        return getMessage(key, DEFAULT, args);
    }

    public String getMessage(final String key, final Personality personality, final Object... args) {
        return new String(messageSource.getMessage(key, args, personality.getLocale()));
    }
}
