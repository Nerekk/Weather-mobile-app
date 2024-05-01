package com.example.weather_mobile_app.WeatherAPI.Models.Forecast;

public class ForecastRecordJSON {
    private String weekDay;
    private String humidity;
    private String icon;
    private String temp;

    public ForecastRecordJSON(String weekDay, String humidity, String icon, String temp) {
        this.weekDay = weekDay;
        this.humidity = humidity;
        this.icon = icon;
        this.temp = temp;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
