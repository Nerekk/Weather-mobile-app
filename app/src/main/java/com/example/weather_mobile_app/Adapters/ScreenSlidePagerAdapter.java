package com.example.weather_mobile_app.Adapters;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragmentAdditional;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragmentBasic;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragmentForecast;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList;

    public ScreenSlidePagerAdapter(FragmentActivity fa, List<Fragment> fragmentList) {
        super(fa);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return fragmentList.get(0);
            case 1:
                return fragmentList.get(1);
            case 2:
                return fragmentList.get(2);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void updateApiCurrent(CurrentWeatherData data) {
        Fragment weather = fragmentList.get(0);
        WeatherFragmentBasic weatherBasic = (WeatherFragmentBasic) weather.getChildFragmentManager().findFragmentByTag("basicFragment");
        Log.i("FRAGMENT", String.valueOf(weatherBasic==null));
        weatherBasic.updateData(data);

        WeatherFragmentAdditional weatherAdditional = (WeatherFragmentAdditional) weather.getChildFragmentManager().findFragmentByTag("additionalFragment");
        weatherAdditional.updateData(data);
    }

    public void updateApiCurrent(CurrentWeatherJsonHolder data) {
        Fragment weather = fragmentList.get(0);
        WeatherFragmentBasic weatherBasic = (WeatherFragmentBasic) weather.getChildFragmentManager().findFragmentByTag("basicFragment");
        Log.i("FRAGMENT", String.valueOf(weatherBasic==null));
        weatherBasic.updateData(data);

        WeatherFragmentAdditional weatherAdditional = (WeatherFragmentAdditional) weather.getChildFragmentManager().findFragmentByTag("additionalFragment");
        weatherAdditional.updateData(data);
    }

    public CurrentWeatherJsonHolder getApiCurrent() {
        Fragment weather = fragmentList.get(0);
        WeatherFragmentBasic wB = (WeatherFragmentBasic) weather.getChildFragmentManager().findFragmentByTag("basicFragment");
        WeatherFragmentAdditional wA = (WeatherFragmentAdditional) weather.getChildFragmentManager().findFragmentByTag("additionalFragment");

        CurrentWeatherJsonHolder holder = new CurrentWeatherJsonHolder();

        holder.setName(wB.getTvCity());
        holder.setCoords(wB.getTvCoords());
        holder.setDate(wB.getTvClock());
        holder.setIcon(wB.getIvWeather());
        holder.setDesc(wB.getTvDesc());
        holder.setTemp(wB.getTvTemp());
        holder.setWindDegree(wA.getIvWindArrowDegree());
        holder.setWind(wA.getTvWind());
        holder.setHumidity(wA.getTvHumidity());
        holder.setVisibility(wA.getTvVisibility());
        holder.setPressure(wA.getTvPressure());

        return holder;
    }

    public void updateApiForecast(ForecastWeatherData data) {
        Fragment weather = fragmentList.get(0);

        WeatherFragmentForecast weatherForecast = (WeatherFragmentForecast) weather.getChildFragmentManager().findFragmentByTag("forecastFragment");
        weatherForecast.updateData(data);
    }

    public void updateApiForecast(ForecastWeatherJsonHolder data) {
        Fragment weather = fragmentList.get(0);

        WeatherFragmentForecast weatherForecast = (WeatherFragmentForecast) weather.getChildFragmentManager().findFragmentByTag("forecastFragment");
        weatherForecast.updateData(data);
    }

    public ForecastWeatherJsonHolder getApiForecast() {
        Fragment weather = fragmentList.get(0);
        WeatherFragmentForecast wF = (WeatherFragmentForecast) weather.getChildFragmentManager().findFragmentByTag("forecastFragment");

        ForecastWeatherJsonHolder holder = new ForecastWeatherJsonHolder(AppConfig.getCurrentLoc());
        List<ForecastRecordJsonHolder> records = new ArrayList<>();

        List<String> days = wF.getTvDays();
        List<String> hums = wF.getTvHums();
        List<String> ivDays = wF.getIvDays();
        List<String> tempDays = wF.getTvTempDays();

        for (int i = 0; i < 5; i++) {
            records.add(new ForecastRecordJsonHolder(
                    days.get(i),
                    hums.get(i),
                    ivDays.get(i),
                    tempDays.get(i)));
        }
        holder.setRecords(records);
        return holder;
    }
}
