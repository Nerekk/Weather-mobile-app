package com.example.weather_mobile_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_mobile_app.R;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;

public class ScreenSlideFragment extends Fragment {

    private int layoutId;

    public ScreenSlideFragment(int id) {
        this.layoutId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    public void updateApi(CurrentWeatherData data) {
//        if (layoutId == R.layout.fragment_weather_main) {
//            WeatherFragmentBasic weatherFragmentBasic = (WeatherFragmentBasic) getChildFragmentManager().findFragmentByTag("basicFragment");
//            Log.i("FRAGMENT", String.valueOf(weatherFragmentBasic==null));
//        }
//    }

}
