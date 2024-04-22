package com.example.weather_mobile_app.WeatherAPI.Models.Current;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentWeatherData {

    @SerializedName("coord")
    CoordClass coord;

    @SerializedName("weather")
    List<WeatherClass> weather;

    @SerializedName("main")
    MainClass main;

    @SerializedName("visibility")
    Integer visibility;

    @SerializedName("wind")
    WindClass wind;

    @SerializedName("name")
    String name;

    @SerializedName("cod")
    Integer cod;

    public CoordClass getCoord() {
        return coord;
    }

    public void setCoord(CoordClass coord) {
        this.coord = coord;
    }

    public List<WeatherClass> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherClass> weather) {
        this.weather = weather;
    }

    public MainClass getMain() {
        return main;
    }

    public void setMain(MainClass main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public WindClass getWind() {
        return wind;
    }

    public void setWind(WindClass wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }





}
