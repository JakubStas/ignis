package com.jakubstas.ignis.reactions.reaction;

public enum Reactions {
    FERTILIZED("Fertilized"),
    FERTILIZING("Fertilizing"),
    LOW_TEMPERATURE("Low Temperature"),
    SUNNY_DAY("Sunny Day"),
    WATERED("Watered"),
    WATERING("Watering");

    private String name;

    Reactions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
