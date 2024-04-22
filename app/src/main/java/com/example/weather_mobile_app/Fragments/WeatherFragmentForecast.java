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
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;

import java.util.ArrayList;
import java.util.List;

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
            int ivNightId = r.getIdentifier("ivNight" + i, "id", name);
            int tvTempDayId = r.getIdentifier("tvTempDay" + i, "id", name);
            int tvTempNightId = r.getIdentifier("tvTempNight" + i, "id", name);
            tvDays.add(getView().findViewById(tvDayId));
            tvHums.add(getView().findViewById(tvHumId));
            ivDays.add(getView().findViewById(ivDayId));
            ivNights.add(getView().findViewById(ivNightId));
            tvTempDays.add(getView().findViewById(tvTempDayId));
            tvTempNights.add(getView().findViewById(tvTempNightId));
        }
    }


    public void updateData(ForecastWeatherData data) {

    }
}
