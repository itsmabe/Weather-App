package com.me.myweatherapp.model;

public class Weather {

    private String city;
    private String region;
    private String date;
    private String temperature;
    private String state;
    private String humidity;
    private String wind;
    private String pressure;

    public Weather(String city, String region, String date, String temperature, String state) {
        this.city = city;
        this.region = region;
        this.date = date;
        this.temperature = temperature;
        this.state = state;
    }

    public Weather(String temperature, String humidity, String wind, String pressure) {
        this.humidity = humidity;
        this.wind = wind;
        this.pressure = pressure;
        this.temperature = temperature;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
}
