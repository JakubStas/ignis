package com.jakubstas.ignis.personality;

import java.util.Locale;

public enum Personality {

    DEFAULT(Locale.ENGLISH);

    private Locale locale;

    Personality(final Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
