package com.example.weather_mobile_app.Interfaces;

import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestWeatherService {
    @GET("weather?appid=95e84a749af41c408381fbc6f39a0e0a")
    Call<CurrentWeatherData> getCurrentWeather(@Query("units") String units, @Query("q") String city);

    @GET("forecast?appid=95e84a749af41c408381fbc6f39a0e0a")
    Call<ForecastWeatherData> getForecastWeather(@Query("units") String units, @Query("q") String city);
}
