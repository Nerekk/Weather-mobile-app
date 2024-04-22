package com.example.weather_mobile_app.Interfaces;

import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestWeatherService {
    @GET("weather?appid=95e84a749af41c408381fbc6f39a0e0a&units=metric")
    Call<CurrentWeatherData> getCurrentWeather(@Query("q") String city);
}
