package com.example.weather_mobile_app.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.Interfaces.WeatherFragmentService;
import com.example.weather_mobile_app.R;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecord;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

public class WeatherFragmentForecast extends Fragment implements WeatherFragmentService {
    List<TextView> tvDays;
    List<TextView> tvHums;
    List<ImageView> ivDays;
    List<ImageView> ivNights;
    List<TextView> tvTempDays;
    List<TextView> tvTempNights;

    public WeatherFragmentForecast() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignIds();
    }

    @Override
    public void assignIds() {
        tvDays = new ArrayList<>();
        tvHums = new ArrayList<>();
        ivDays = new ArrayList<>();
        ivNights = new ArrayList<>();
        tvTempDays = new ArrayList<>();
        tvTempNights = new ArrayList<>();
        Log.i("Lokacja", getView().getResources().getResourcePackageName(R.id.tvDay1));

        Resources r = getView().getResources();
        String name = getView().getResources().getResourcePackageName(R.id.tvDay1);
        for(int i = 1; i < 6; i++) {
            int tvDayId = r.getIdentifier("tvDay" + i, "id", name);
            int tvHumId = r.getIdentifier("tvHum" + i, "id", name);
            int ivDayId = r.getIdentifier("ivDay" + i, "id", name);
            int tvTempDayId = r.getIdentifier("tvTempDay" + i, "id", name);
            tvDays.add(getView().findViewById(tvDayId));
            tvHums.add(getView().findViewById(tvHumId));
            ivDays.add(getView().findViewById(ivDayId));
            tvTempDays.add(getView().findViewById(tvTempDayId));
        }
    }


    public void updateData(ForecastWeatherData data) {
        int index = 0;
        List<ForecastRecord> records = data.getList();
        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(data.getCity().getTimezone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        fillForecast(records, formatter, zoneOffset, index);
    }

    private void fillForecast(List<ForecastRecord> records, DateTimeFormatter formatter, ZoneOffset zoneOffset, int index) {
        for (ForecastRecord record : records) {
            LocalDateTime localDateTime = LocalDateTime.parse(record.getDt_txt(), formatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.ofOffset("UTC", zoneOffset));
            ZonedDateTime nowInZone = ZonedDateTime.now(zoneOffset);

            if (zonedDateTime.getHour() == 15) {
                Log.i("FORECAST", String.valueOf(zonedDateTime.getHour()) + ":" + zonedDateTime.getMinute());
                Log.i("FORECAST SET INDEX", String.valueOf(index));
                setDayName(index, zonedDateTime, nowInZone);
                setHumidity(index, record);
                setDayTemperature(index, record);
                index++;
            }
        }
    }

    private void setDayTemperature(int index, ForecastRecord record) {
        String temperature = String.valueOf(record.getMain().getTemp().intValue()) + WeatherConstants.DEGREES;
        tvTempDays.get(index).setText(temperature);
    }

    private void setHumidity(int index, ForecastRecord record) {
        String humidity = record.getMain().getHumidity().toString() + "%";
        tvHums.get(index).setText(humidity);
    }

    private void setDayName(int index, ZonedDateTime zonedDateTime, ZonedDateTime nowInZone) {
        if (zonedDateTime.toLocalDate().equals(nowInZone.toLocalDate())) {
            tvDays.get(index).setText("Today");
        } else {
            tvDays.get(index).setText(getDayName(zonedDateTime));
        }
    }

    private String getDayName(ZonedDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        return zonedDateTime.format(formatter);
    }
}
