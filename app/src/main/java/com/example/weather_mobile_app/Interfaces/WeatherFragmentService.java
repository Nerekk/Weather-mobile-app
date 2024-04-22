package com.example.weather_mobile_app.Interfaces;

import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;

public interface WeatherFragmentService {
    void assignIds();
    void updateData(CurrentWeatherData data);
}
