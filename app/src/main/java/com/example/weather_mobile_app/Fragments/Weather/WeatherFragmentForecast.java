package com.example.weather_mobile_app.Fragments.Weather;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.Interfaces.WeatherFragmentService;
import com.example.weather_mobile_app.R;
import com.example.weather_mobile_app.Utils.WeatherIcons;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecord;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WeatherFragmentForecast extends Fragment implements WeatherFragmentService {
    List<TextView> tvDays;
    List<TextView> tvHums;
    List<ImageView> ivDays;
    List<TextView> tvTempDays;

    public WeatherFragmentForecast() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignIds();
    }

    @Override
    public void assignIds() {
        tvDays = new ArrayList<>();
        tvHums = new ArrayList<>();
        ivDays = new ArrayList<>();
        tvTempDays = new ArrayList<>();
        Log.i("Lokacja", getView().getResources().getResourcePackageName(R.id.tvDay1));

        Resources r = getView().getResources();
        String name = getView().getResources().getResourcePackageName(R.id.tvDay1);
        for(int i = 1; i < 6; i++) {
            int tvDayId = r.getIdentifier("tvDay" + i, "id", name);
            int tvHumId = r.getIdentifier("tvHum" + i, "id", name);
            int ivDayId = r.getIdentifier("ivDay" + i, "id", name);
            int tvTempDayId = r.getIdentifier("tvTempDay" + i, "id", name);
            tvDays.add(getView().findViewById(tvDayId));
            tvHums.add(getView().findViewById(tvHumId));
            ivDays.add(getView().findViewById(ivDayId));
            tvTempDays.add(getView().findViewById(tvTempDayId));
        }
    }


    public void updateData(ForecastWeatherData data) {
        int index = 0;
        List<ForecastRecord> records = data.getList();
        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(data.getCity().getTimezone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        fillForecast(records, formatter, zoneOffset, index);
    }
    public void updateData(ForecastWeatherJsonHolder data) {
        int index = 0;
        List<ForecastRecordJsonHolder> records = data.getRecords();
        for (ForecastRecordJsonHolder record : records) {
            tvTempDays.get(index).setText(record.getTemp());

            tvHums.get(index).setText(record.getHumidity());

            tvDays.get(index).setText(record.getWeekDay());

            // ustaw icon
            String icon = record.getIcon();
            ivDays.get(index).setTag(icon);
            ivDays.get(index).setImageResource(WeatherIcons.getIconResource(icon));

            index++;
        }
    }

    private void fillForecast(List<ForecastRecord> records, DateTimeFormatter formatter, ZoneOffset zoneOffset, int index) {
        for (ForecastRecord record : records) {
            LocalDateTime localDateTime = LocalDateTime.parse(record.getDt_txt(), formatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.ofOffset("UTC", zoneOffset));
            ZonedDateTime nowInZone = ZonedDateTime.now(zoneOffset);

            if (zonedDateTime.getHour() == 12) {
//                Log.i("FORECAST", String.valueOf(zonedDateTime.getHour()) + ":" + zonedDateTime.getMinute());
//                Log.i("FORECAST SET INDEX", String.valueOf(index));
                setDayName(index, zonedDateTime, nowInZone);
                setHumidity(index, record);
                setDayTemperature(index, record);

                String icon = record.getWeather().get(0).getIcon();
                ivDays.get(index).setTag(icon);
                ivDays.get(index).setImageResource(WeatherIcons.getIconResource(icon));

                index++;
            }
        }
    }

    private void setDayTemperature(int index, ForecastRecord record) {
        String temperature = String.valueOf(record.getMain().getTemp().intValue()) + AppConfig.DEGREES;
        tvTempDays.get(index).setText(temperature);
    }

    private void setHumidity(int index, ForecastRecord record) {
        String humidity = record.getMain().getHumidity().toString() + "%";
        tvHums.get(index).setText(humidity);
    }

    private void setDayName(int index, ZonedDateTime zonedDateTime, ZonedDateTime nowInZone) {
        if (zonedDateTime.toLocalDate().equals(nowInZone.toLocalDate())) {
            tvDays.get(index).setText("Today");
        } else {
            tvDays.get(index).setText(getDayName(zonedDateTime));
        }
    }

    private String getDayName(ZonedDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        return zonedDateTime.format(formatter);
    }

    public List<String> getTvDays() {
        List<String> list = new ArrayList<>();
        for (TextView tv : tvDays) {
            list.add(tv.getText().toString());
        }
        return list;
    }

    public List<String> getTvHums() {
        List<String> list = new ArrayList<>();
        for (TextView tv : tvHums) {
            list.add(tv.getText().toString());
        }
        return list;
    }

    public List<String> getIvDays() {
        List<String> list = new ArrayList<>();
        for (ImageView iv : ivDays) {
            list.add((String) iv.getTag());
        }
        return list;
    }

    public List<String> getTvTempDays() {
        List<String> list = new ArrayList<>();
        for (TextView tv : tvTempDays) {
            list.add(tv.getText().toString());
        }
        return list;
    }
}
