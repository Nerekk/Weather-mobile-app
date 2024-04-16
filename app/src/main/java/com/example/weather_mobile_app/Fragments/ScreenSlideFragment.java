package com.example.weather_mobile_app.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_mobile_app.Adapters.WeatherRecyclerAdapter;
import com.example.weather_mobile_app.R;

public class ScreenSlideFragment extends Fragment {

    private int layoutId;
    private RecyclerView recyclerView;
    private WeatherRecyclerAdapter weatherRecyclerAdapter;
    private boolean isWeather;

    public ScreenSlideFragment(int id, boolean isWeather) {
        this.layoutId = id;
        this.isWeather = isWeather;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("onViewCreated", "START");
        if (isWeather) {
            Log.i("onViewCreated", "IF");
            String[] data = new String[10];
            data[0] = "Pierwszy";
            data[1] = "Drugi";
            data[2] = "Trzeci";
            data[3] = "Czwarty";
            data[4] = "Piąty";
            data[5] = "Szosty";
            data[6] = "Siodmy";
            data[7] = "Osmy";
            data[8] = "Dziewiaty";
            data[9] = "Dziesiaty";
            weatherRecyclerAdapter = new WeatherRecyclerAdapter(data);
            recyclerView = view.findViewById(R.id.recyclerWeather);
            Log.i("onViewCreated", "ADAPTER");
            recyclerView.setAdapter(weatherRecyclerAdapter);
        }
        Log.i("onViewCreated", "END");
    }

}
