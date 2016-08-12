package com.jakubstas.ignis.readings.model;

import com.jakubstas.ignis.reactions.reaction.Reactions;

public class Reaction {

    private boolean reacted = false;

    private String reaction = "none";

    public void reactedWithFertilized() {
        reaction = Reactions.FERTILIZED.getName();
        reacted = true;
    }

    public void reactedWithFertilizing() {
        reaction = Reactions.FERTILIZING.getName();
        reacted = true;
    }

    public void reactedWithLowTemperature() {
        reaction = Reactions.LOW_TEMPERATURE.getName();
        reacted = true;
    }

    public void reactedWithSunnyDay() {
        reaction = Reactions.SUNNY_DAY.getName();
        reacted = true;
    }

    public void reactedWithWatered() {
        reaction = Reactions.WATERED.getName();
        reacted = true;
    }

    public void reactedWithWatering() {
        reaction = Reactions.WATERING.getName();
        reacted = true;
    }

    public boolean isReacted() {
        return reacted;
    }

    public String getReaction() {
        return reaction;
    }

    @Override
    public String toString() {
        return "Reaction{" +
                "reacted=" + reacted +
                ", reaction='" + reaction + '\'' +
                '}';
    }
}