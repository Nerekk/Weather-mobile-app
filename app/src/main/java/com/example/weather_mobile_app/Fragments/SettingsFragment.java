package com.example.weather_mobile_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.MainActivity;
import com.example.weather_mobile_app.R;

public class SettingsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_main, container, false);
        prepareComponents(view);

        return view;
    }

    private static void prepareComponents(View view) {
        ImageView iv = (ImageView) view.findViewById(R.id.ivRefresh);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getMainActivity().getAPIData();
            }
        });

        Switch apiSwitch = (Switch) view.findViewById(R.id.apiSwitch);
        apiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppConfig.isRefreshSwitchEnabled = b;
            }
        });

        Switch unitSwitch = (Switch) view.findViewById(R.id.unitSwitch);
        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppConfig.isUnitsSwitchEnabled = b;
            }
        });
    }
}
