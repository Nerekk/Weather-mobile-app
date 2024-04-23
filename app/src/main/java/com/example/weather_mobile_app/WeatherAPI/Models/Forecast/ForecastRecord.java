package com.example.weather_mobile_app.WeatherAPI.Models.Forecast;

import com.example.weather_mobile_app.WeatherAPI.Models.Current.MainClass;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.WeatherClass;

import java.util.List;

public class ForecastRecord {
    Integer dt;
    MainClass main;
    List<WeatherClass> weather;
    String dt_txt;

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public MainClass getMain() {
        return main;
    }

    public void setMain(MainClass main) {
        this.main = main;
    }

    public List<WeatherClass> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherClass> weather) {
        this.weather = weather;
    }
}
