package com.example.weather_mobile_app.Adapters;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weather_mobile_app.Fragments.WeatherFragmentBasic;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;

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

    public void updateApi(CurrentWeatherData data) {
        Fragment weather = fragmentList.get(0);
        WeatherFragmentBasic weatherBasic = (WeatherFragmentBasic) weather.getChildFragmentManager().findFragmentByTag("basicFragment");
        Log.i("FRAGMENT", String.valueOf(weatherBasic==null));
        weatherBasic.updateData(data);
    }
}
