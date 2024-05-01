package com.example.weather_mobile_app.WeatherAPI.Models.Current;

public class CurrentWeatherJSON {
    private String localization;
    private String coords;
    private String date;
    private String icon;
    private String desc;
    private String temp;
    private int windDegree;
    private String wind;
    private String humidity;
    private String visibility;
    private String pressure;

    public CurrentWeatherJSON(String localization, String coords, String date, String icon, String desc, String temp, int windDegree, String wind, String humidity, String visibility, String pressure) {
        this.localization = localization;
        this.coords = coords;
        this.date = date;
        this.icon = icon;
        this.desc = desc;
        this.temp = temp;
        this.windDegree = windDegree;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.pressure = pressure;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(int windDegree) {
        this.windDegree = windDegree;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
}
