package com.example.weather_mobile_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.Interfaces.WeatherFragmentService;
import com.example.weather_mobile_app.R;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;

public class WeatherFragmentAdditional extends Fragment implements WeatherFragmentService {

    TextView tvWind, tvHumidity, tvVisibility, tvPressure;
    ImageView ivWindArrow;

    public WeatherFragmentAdditional() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignIds();
    }

    @Override
    public void assignIds() {
        tvWind = getView().findViewById(R.id.tvWind);
        tvHumidity = getView().findViewById(R.id.tvHumidity);
        tvVisibility = getView().findViewById(R.id.tvVisibility);
        tvPressure = getView().findViewById(R.id.tvPressure);
        ivWindArrow = getView().findViewById(R.id.ivWindArrow);
    }

    public void updateData(CurrentWeatherData data) {
        Integer speed = ((Double)(data.getWind().getSpeed() * 3.6)).intValue();
        String wind = speed.toString() + "km/h";
        Integer windDegree = data.getWind().getDeg();
        String humidity = data.getMain().getHumidity().toString() + "%";
        String pressure = data.getMain().getPressure().toString() + "mb";
        String visibility = String.valueOf((data.getVisibility().doubleValue()/1000)) + "km";
        tvWind.setText(wind);
        ivWindArrow.setRotation(-windDegree);
        tvHumidity.setText(humidity);
        tvPressure.setText(pressure);
        tvVisibility.setText(visibility);
    }
}
