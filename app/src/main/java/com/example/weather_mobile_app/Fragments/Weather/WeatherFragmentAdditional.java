package com.example.weather_mobile_app.Fragments.Weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.Interfaces.WeatherFragmentService;
import com.example.weather_mobile_app.R;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder;

import java.util.Objects;

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
        Integer speed;
        String wind = null;
        if (Objects.equals(AppConfig.getUnitsType(), AppConfig.IMPERIAL)) {
            speed = data.getWind().getSpeed().intValue();
        } else {
            speed = ((Double)(data.getWind().getSpeed() * 3.6)).intValue();
        }
        switch (AppConfig.getUnitsType()) {
            case AppConfig.DEFAULT:
            case AppConfig.METRIC:
                wind = speed.toString() + "km/h";
                break;
            case AppConfig.IMPERIAL:
                wind = speed.toString() + "mph";
                break;
        }
        Integer windDegree = data.getWind().getDeg();
        String humidity = data.getMain().getHumidity().toString() + "%";
        String pressure = data.getMain().getPressure().toString() + "hPa";

        String visibility = String.valueOf((data.getVisibility().doubleValue()/1000)) + "km";
        tvWind.setText(wind);
        ivWindArrow.setRotation(windDegree);
        tvHumidity.setText(humidity);
        tvPressure.setText(pressure);
        tvVisibility.setText(visibility);
    }

    public void updateData(CurrentWeatherJsonHolder data) {
        Integer speed;
        String wind = null;
        if (Objects.equals(AppConfig.getUnitsType(), AppConfig.IMPERIAL)) {
            speed = data.getWind().intValue();
        } else {
            speed = ((Double)(data.getWind() * 3.6)).intValue();
        }
        switch (AppConfig.getUnitsType()) {
            case AppConfig.DEFAULT:
            case AppConfig.METRIC:
                wind = speed + "km/h";
                break;
            case AppConfig.IMPERIAL:
                wind = speed + "mph";
                break;
        }
        Integer windDegree = data.getWindDegree();
        String humidity = data.getHumidity() + "%";
        String pressure = data.getPressure() + "hPa";

        String visibility = String.valueOf((data.getVisibility().doubleValue()/1000)) + "km";


        tvWind.setText(wind);
        ivWindArrow.setRotation(windDegree);
        tvHumidity.setText(humidity);
        tvPressure.setText(pressure);
        tvVisibility.setText(visibility);
    }
}
