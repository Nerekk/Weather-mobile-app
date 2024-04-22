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

public class WeatherFragmentBasic extends Fragment implements WeatherFragmentService {

    TextView tvTemp, tvCoords, tvDesc, tvCity, tvClock;
    ImageView ivWeather;

    public WeatherFragmentBasic() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather1, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignIds();
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
        String temp = String.valueOf(data.getMain().getTemp().intValue()) + WeatherConstants.DEGREES_CELSIUS;
        String coords1 = String.valueOf(data.getCoord().getLat().intValue()) + WeatherConstants.DEGREES;
        String coords2 = String.valueOf(data.getCoord().getLon().intValue()) + WeatherConstants.DEGREES;
        String coords = "Lat:" + coords1 + " Lon:" + coords2;
        String desc = String.valueOf(data.getWeather().get(0).getMain());
        String city = String.valueOf(data.getName());
//        String clock = String.valueOf(data.getMain().getTemp()) + WeatherConstants.DEGREES_CELSIUS;
        tvTemp.setText(temp);
        tvCoords.setText(coords);
        tvDesc.setText(desc);
        tvCity.setText(city);
    }
}
