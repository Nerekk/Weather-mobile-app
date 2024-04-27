package com.example.weather_mobile_app.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.MainActivity;
import com.example.weather_mobile_app.R;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    private static ArrayList<String> unitList;
    private static ArrayAdapter<String> adapter;
    private static ListView units;
    private static TextView tvUnits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_main, container, false);
        prepareComponents(view);

        return view;
    }

    private static void prepareComponents(View view) {
        tvUnits = view.findViewById(R.id.tvUnits);

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

        unitList = new ArrayList<>();
        unitList.add("Default (Kelvin)");
        unitList.add("Metric (Celsius)");
        unitList.add("Imperial (Fahrenheit)");

        units = (ListView) view.findViewById(R.id.lvUnits);
        adapter = new ArrayAdapter<>(MainActivity.getMainActivity(), android.R.layout.simple_list_item_1, unitList);
        units.setAdapter(adapter);

        updateUnitInfo();
        units.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AppConfig.setUnitsIndex(position);
                MainActivity.getMainActivity().getAPIData();
                updateUnitInfo();
                MainActivity.writeToast("Units set: " + unitList.get(position));
            }
        });
    }

    private static void updateUnitInfo() {
        String unit = "Units: " + AppConfig.getUnitsType();
        tvUnits.setText(unit);
    }
}
