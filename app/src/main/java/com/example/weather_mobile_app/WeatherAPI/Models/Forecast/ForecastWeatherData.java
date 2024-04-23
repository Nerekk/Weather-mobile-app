package com.example.weather_mobile_app.WeatherAPI.Models.Forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastWeatherData {
    @SerializedName("cod")
    Integer cod;

    @SerializedName("message")
    Integer message;

    @SerializedName("cnt")
    Integer cnt;

    @SerializedName("list")
    List<ForecastRecord> list;

    @SerializedName("city")
    CityClass city;

    public CityClass getCity() {
        return city;
    }

    public void setCity(CityClass city) {
        this.city = city;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public List<ForecastRecord> getList() {
        return list;
    }

    public void setList(List<ForecastRecord> list) {
        this.list = list;
    }
}
