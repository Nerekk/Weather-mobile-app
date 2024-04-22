package com.example.weather_mobile_app.WeatherAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestWeatherService {
//    @GET("data/2.5/weather?q={city}&units=metric&appid=95e84a749af41c408381fbc6f39a0e0a")
//    Call<CurrentWeatherData> getCurrentWeather(@Path("city") String city);

    @GET("weather?appid=95e84a749af41c408381fbc6f39a0e0a&units=metric")
    Call<CurrentWeatherData> getCurrentWeather(@Query("q") String city);
}
