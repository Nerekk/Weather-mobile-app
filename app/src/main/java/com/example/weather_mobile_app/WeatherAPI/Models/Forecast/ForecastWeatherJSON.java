package com.example.weather_mobile_app.WeatherAPI.Models.Forecast;

import java.util.ArrayList;
import java.util.List;

public class ForecastWeatherJSON {
    private String localization;
    private List<ForecastRecordJSON> records;
    private int i;

    public ForecastWeatherJSON(String localization) {
        this.localization = localization;
        this.records = new ArrayList<>();
        this.i = 0;
    }

    public void addRecord(ForecastRecordJSON record) {
        if (i < 5) {
            records.add(record);
            i++;
        }
    }

    public List<ForecastRecordJSON> getRecords() {
        return records;
    }
}
