package com.jakubstas.ignis.model;

public class Readings {

    private int moisture;

    private int temperature;

    private int humidity;

    private int light;

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

    public String getAsTweet() {
        return "moisture=" + moisture +
                "|temperature=" + temperature +
                "|humidity=" + humidity +
                "|light=" + light;
    }
}
