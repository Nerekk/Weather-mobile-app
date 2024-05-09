package com.example.weather_mobile_app.Fragments.Weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.Fragments.FavouritesFragment;
import com.example.weather_mobile_app.Interfaces.WeatherFragmentService;
import com.example.weather_mobile_app.MainActivity;
import com.example.weather_mobile_app.R;
import com.example.weather_mobile_app.Utils.WeatherIcons;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherFragmentBasic extends Fragment implements WeatherFragmentService {

    TextView tvTemp, tvCoords, tvDesc, tvCity, tvClock;
    ImageView ivWeather;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather1, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignIds();
        updateData(MainActivity.getMainActivity().getCurrentDataOffline());
    }

    @Override
    public void assignIds() {
        tvTemp = getView().findViewById(R.id.tvTemp);
        tvCoords = getView().findViewById(R.id.tvCoords);
        tvDesc = getView().findViewById(R.id.tvDesc);
        tvCity = getView().findViewById(R.id.tvCity);
        tvClock = getView().findViewById(R.id.tvClock);
        ivWeather = getView().findViewById(R.id.ivWeather);
    }

    public void updateData(CurrentWeatherData data) {
        String temp = data.getMain().getTemp().intValue() + AppConfig.getDegreesType();

        String coords1 = getCoordsFormatting(data.getCoord().getLat(), "S", "N");
        String coords2 = getCoordsFormatting(data.getCoord().getLon(), "W", "E");
        String coords = coords1 + " " + coords2;

        String desc = String.valueOf(data.getWeather().get(0).getMain());
        String city = FavouritesFragment.getSetLoc();
        String icon = data.getWeather().get(0).getIcon();

        String clock = convertTime(data);
        tvTemp.setText(temp);
        tvCoords.setText(coords);
        tvDesc.setText(desc);
        tvCity.setText(city);
        tvClock.setText(clock);
        ivWeather.setTag(icon);
        ivWeather.setImageResource(WeatherIcons.getIconResource(icon));
    }

    @NonNull
    private static String getCoordsFormatting(Double data, String S, String N) {
        String coords;
        if (data.intValue() < 0) {
            coords = data.intValue() + AppConfig.DEGREES + S;
            coords = coords.substring(1);
        } else {
            coords = data.intValue() + AppConfig.DEGREES + N;
        }
        return coords;
    }

    public void updateData(CurrentWeatherJsonHolder data) {
        tvTemp.setText(data.getTemp());
        tvCoords.setText(data.getCoords());
        tvDesc.setText(data.getDesc());
        tvCity.setText(data.getName());
        tvClock.setText(data.getDate());

        String icon = data.getIcon();
        ivWeather.setTag(icon);
        ivWeather.setImageResource(WeatherIcons.getIconResource(icon));
    }

    public static String convertTime(CurrentWeatherData data) {
        int dt = data.getDt();
        int timezone = data.getTimezone();

        Instant instant = Instant.ofEpochSecond(dt);

        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(timezone);

        ZonedDateTime zonedDateTime = instant.atZone(zoneOffset);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return zonedDateTime.format(formatter);
    }

    public String getTvTemp() {
        return tvTemp.getText().toString();
    }

    public String getTvCoords() {
        return tvCoords.getText().toString();
    }

    public String getTvDesc() {
        return tvDesc.getText().toString();
    }

    public String getTvCity() {
        return tvCity.getText().toString();
    }

    public String getTvClock() {
        return tvClock.getText().toString();
    }

    public String getIvWeather() {
        return (String)ivWeather.getTag();
    }
}
