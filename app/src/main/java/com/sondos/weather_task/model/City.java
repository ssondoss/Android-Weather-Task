package com.sondos.weather_task.model;

public class City {

    private String name;
    private float tempC;
    private float tempF;

    public City() {
    }

    public City(String name, float tempC, float tempF) {
        this.name = name;
        this.tempC = tempC;
        this.tempF = tempF;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTempC() {
        return tempC;
    }

    public void setTempC(float tempC) {
        this.tempC = tempC;
    }

    public float getTempF() {
        return tempF;
    }

    public void setTempF(float tempF) {
        this.tempF = tempF;
    }
}
