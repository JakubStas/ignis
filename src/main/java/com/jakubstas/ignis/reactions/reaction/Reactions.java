package com.jakubstas.ignis.reactions.reaction;

public enum Reactions {
    FERTILIZED("fertilized"),
    FERTILIZING("fertilizing"),
    LOW_TEMPERATURE("low_temperature"),
    SUNNY_DAY("sunny_day"),
    WATERED("watered"),
    WATERING("watering");

    private String name;

    Reactions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
