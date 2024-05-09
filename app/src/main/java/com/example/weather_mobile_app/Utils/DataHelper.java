package com.example.weather_mobile_app.Utils;

import static com.example.weather_mobile_app.MainActivity.JSON_CURRENT;
import static com.example.weather_mobile_app.MainActivity.JSON_FORECAST;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_COORDS;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_DATE;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_DESC;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_HUMIDITY;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_ICON;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_NAME;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_PRESSURE;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_TEMP;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_VISIBILITY;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_WIND;
import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.C_WIND_DEGREE;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder.F_HUMIDITY;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder.F_ICON;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder.F_TEMP;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder.F_WEEK_DAY;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder.F_NAME;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder.F_RECORDS;

import androidx.annotation.NonNull;

import com.example.weather_mobile_app.AppConfig;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecord;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    public static void saveOrUpdateJSON(CurrentWeatherJsonHolder data) {
        JSONArray jsonArray;
        try {
            String response = getJsonArrayFromFile(JSON_CURRENT);
            jsonArray = new JSONArray(response);
        } catch (IOException | JSONException e) {
            jsonArray = new JSONArray();
        }

        JSONObject localization = getJsonLocalization(createJsonObject(data), jsonArray);

        jsonArray.put(localization);

        saveJsonToFile(JSON_CURRENT, jsonArray);

    }

    public static void saveOrUpdateJSON(ForecastWeatherJsonHolder data) {
        JSONArray jsonArray;
        try {
            String response = getJsonArrayFromFile(JSON_FORECAST);
            jsonArray = new JSONArray(response);

        } catch (IOException | JSONException e) {
            jsonArray = new JSONArray();
        }

        JSONObject localization = getJsonLocalization(createJsonObject(data), jsonArray);

        jsonArray.put(localization);

        saveJsonToFile(JSON_FORECAST, jsonArray);
    }

    public static void removeDataJSON(String filename, String name) {
        JSONArray jsonArray;
        try {
            String response = getJsonArrayFromFile(filename);
            jsonArray = new JSONArray(response);

        } catch (IOException | JSONException e) {
            jsonArray = new JSONArray();
        }

        int existingIndex = findExistingLocationIndex(jsonArray, name);

        if (existingIndex != -1) {
            jsonArray.remove(existingIndex);
        }

        saveJsonToFile(filename, jsonArray);
    }

    public static ForecastWeatherJsonHolder loadForecastJSON() {
        try {
            String response = getJsonArrayFromFile(JSON_FORECAST);
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject locationJson = jsonArray.getJSONObject(i);
                String cityName = locationJson.getString(C_NAME);
                if (cityName.equals(AppConfig.getCurrentLoc())) {
                    return jsonToHolderTransferForecast(locationJson);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new ForecastWeatherJsonHolder();
    }

    public static CurrentWeatherJsonHolder loadCurrentJSON() {
        try {
            String response = getJsonArrayFromFile(JSON_CURRENT);
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject locationJson = jsonArray.getJSONObject(i);
                String cityName = locationJson.getString(C_NAME);
                if (cityName.equals(AppConfig.getCurrentLoc())) {
                    return jsonToHolderTransfer(locationJson);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return new CurrentWeatherJsonHolder();
    }

    // VVV less important methods VVV

    private static int findExistingLocationIndex(JSONArray jsonArray, String name) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject existingLocationJson = jsonArray.getJSONObject(i);
                if (existingLocationJson.getString("name").equals(name)) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private static void saveJsonToFile(String filename, JSONArray jsonArray) {
        File file = new File(AppConfig.PATH_DIR, filename);
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonArray.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JSONObject getJsonLocalization(JSONObject data, JSONArray jsonArray) {
        JSONObject localization = data;
        int existingIndex = findExistingLocationIndex(jsonArray, AppConfig.getCurrentLoc());

        if (existingIndex != -1) {
            jsonArray.remove(existingIndex);
        }
        return localization;
    }

    @NonNull
    private static String getJsonArrayFromFile(String filename) throws IOException {
        File file = new File(AppConfig.PATH_DIR, filename);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static ForecastWeatherJsonHolder dataToHolderTransfer(ForecastWeatherData data) {
        List<ForecastRecordJsonHolder> records = new ArrayList<>();
        String name = data.getCity().getName();
        for (ForecastRecord record : data.getList()) {
            records.add(new ForecastRecordJsonHolder(
                    record.getDt_txt(),
                    String.valueOf(record.getMain().getHumidity()),
                    record.getWeather().get(0).getIcon(),
                    String.valueOf(record.getMain().getTemp().intValue())));
        }

        ForecastWeatherJsonHolder holder = new ForecastWeatherJsonHolder(name);
        holder.setRecords(records);
        return holder;
    }

    public static CurrentWeatherJsonHolder jsonToHolderTransfer(JSONObject data) {
        CurrentWeatherJsonHolder holder;
        try {
            String name = data.getString(C_NAME);
            String coords = data.getString(C_COORDS);
            String date = data.getString(C_DATE);
            String icon = data.getString(C_ICON);
            String desc = data.getString(C_DESC);
            String temp = data.getString(C_TEMP);
            Integer degree = data.getInt(C_WIND_DEGREE);
            String wind = data.getString(C_WIND);
            String humidity = data.getString(C_HUMIDITY);
            String visibility = data.getString(C_VISIBILITY);
            String pressure = data.getString(C_PRESSURE);

            holder = new CurrentWeatherJsonHolder(
                    name,
                    coords,
                    date,
                    icon,
                    desc,
                    temp,
                    degree,
                    wind,
                    humidity,
                    visibility,
                    pressure
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return holder;
    }

    public static ForecastWeatherJsonHolder jsonToHolderTransferForecast(JSONObject data) {
        ForecastWeatherJsonHolder holder;
        try {
            String name = data.getString(C_NAME);
            JSONArray jsonArray = data.getJSONArray(F_RECORDS);
            List<ForecastRecordJsonHolder> records = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                records.add(new ForecastRecordJsonHolder(
                        object.getString(F_WEEK_DAY),
                        object.getString(F_HUMIDITY),
                        object.getString(F_ICON),
                        object.getString(F_TEMP)
                ));
            }

            holder = new ForecastWeatherJsonHolder(name);
            holder.setRecords(records);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return holder;
    }

    private static JSONObject createJsonObject(CurrentWeatherJsonHolder data) {
        JSONObject object = new JSONObject();
        try {
            object.put(C_NAME, data.getName());
            object.put(C_COORDS, data.getCoords());
            object.put(C_DATE, data.getDate());
            object.put(C_ICON, data.getIcon());
            object.put(C_DESC, data.getDesc());
            object.put(C_TEMP, data.getTemp());
            object.put(C_WIND_DEGREE, data.getWindDegree());
            object.put(C_WIND, data.getWind());
            object.put(C_HUMIDITY, data.getHumidity());
            object.put(C_VISIBILITY, data.getVisibility());
            object.put(C_PRESSURE, data.getPressure());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private static JSONObject createJsonObject(ForecastWeatherJsonHolder data) {
        JSONObject object = new JSONObject();
        try {
            object.put(F_NAME, data.getName());

            JSONArray forecastsJson = new JSONArray();
            for (ForecastRecordJsonHolder record : data.getRecords()) {
                JSONObject forecastJson = new JSONObject();
                forecastJson.put(F_WEEK_DAY, record.getWeekDay());
                forecastJson.put(F_HUMIDITY, record.getHumidity());
                forecastJson.put(F_ICON, record.getIcon());
                forecastJson.put(F_TEMP, record.getTemp());
                forecastsJson.put(forecastJson);
            }
            object.put(F_RECORDS, forecastsJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
