package com.example.weather_mobile_app.WeatherAPI.Models.Forecast;

import java.util.ArrayList;
import java.util.List;

public class ForecastWeatherJsonHolder {

    public final static String F_NAME = "name";
    private String name;

    public final static String F_RECORDS = "records";
    private List<ForecastRecordJsonHolder> records;
    private int i;

    public ForecastWeatherJsonHolder(String name) {
        this.name = name;
        this.records = new ArrayList<>();
        this.i = 0;
    }

    public void addRecord(ForecastRecordJsonHolder record) {
        if (i < 5) {
            records.add(record);
            i++;
        }
    }

    public List<ForecastRecordJsonHolder> getRecords() {
        return records;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecords(List<ForecastRecordJsonHolder> records) {
        this.records = records;
    }
}
