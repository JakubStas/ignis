package com.jakubstas.ignis.personalities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import static com.jakubstas.ignis.personalities.Personality.DEFAULT;

@Component
public class PersonalityMessageSource {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(final String key, final Object... args) {
        return messageSource.getMessage(key, args, DEFAULT.getLocale());
    }

    public String getMessage(final String key, final Personality personality, final Object... args) {
        return messageSource.getMessage(key, args, personality.getLocale());
    }
}
