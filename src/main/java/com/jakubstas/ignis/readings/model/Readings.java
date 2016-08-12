package com.jakubstas.ignis.readings.model;

public class Readings {

    private int moisture;

    private int temperature;

    private int humidity;

    private int light;

    private System system;

    private Reaction reaction;

    public Readings() {
        this.system = new System();
        this.reaction = new Reaction();
    }

    public int getMoisture() {
        return moisture;
    }

    public void setMoisture(int moisture) {
        this.moisture = moisture;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public System getSystem() {
        return system;
    }

    public Reaction getReaction() {
        return reaction;
    }

    public String getAsTweet() {
        return "moisture=" + moisture + "|temperature=" + temperature + "|humidity=" + humidity + "|light=" + light;
    }

    @Override
    public String toString() {
        return "Readings{" +
                "moisture=" + moisture +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", light=" + light +
                ", system=" + system +
                ", reaction=" + reaction +
                '}';
    }
}