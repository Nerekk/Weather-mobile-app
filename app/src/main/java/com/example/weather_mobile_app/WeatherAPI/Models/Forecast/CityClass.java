package com.example.weather_mobile_app.WeatherAPI.Models.Forecast;

public class CityClass {
    String name;
    Integer timezone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }
}
