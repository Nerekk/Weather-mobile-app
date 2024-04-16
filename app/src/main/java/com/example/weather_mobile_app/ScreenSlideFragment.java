package com.example.weather_mobile_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ScreenSlideFragment extends Fragment {

    private int layoutId;

    public ScreenSlideFragment(int id) {
        this.layoutId = id;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(
                layoutId, container, false);
    }
}
