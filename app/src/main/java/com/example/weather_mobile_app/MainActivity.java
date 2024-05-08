package com.example.weather_mobile_app;

import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.*;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder.*;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weather_mobile_app.Adapters.ScreenSlidePagerAdapter;
import com.example.weather_mobile_app.Fragments.FavouritesFragment;
import com.example.weather_mobile_app.Fragments.SettingsFragment;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragment;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragmentBasic;
import com.example.weather_mobile_app.Interfaces.CityNameListener;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;
import com.example.weather_mobile_app.Interfaces.RequestWeatherService;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecord;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String SP_FILE = "MyPrefsFile";
    private static final String JSON_CURRENT = "SavedCurrentLocations.json";
    private static final String JSON_FORECAST = "SavedForecastLocations.json";
    private static final int TYPE_CURR = 0;
    private static final int TYPE_FORE = 1;

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private FragmentStateAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    public static MainActivity mainActivity;

    RequestWeatherService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loadSharedPreferences();

        createFragmentList();

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPagerHandle();
        setBottomNavViewListeners();

        mainActivity = this;

        prepareAPIService();
        getAPIData();


//        ((ScreenSlidePagerAdapter)pagerAdapter).updateApi(dataPack);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSharedPreferences();
        Log.i("ONRESUME", "ONR");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPreferences();
        Log.i("ONPAUSE", "ONP");
    }

    private void saveSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(SP_FILE, MODE_PRIVATE).edit();
         // "key" to nazwa klucza, a myValue to wartość do zapisania

        editor.putInt("keyInit", 1);

        editor.putBoolean("keySwitch", AppConfig.isRefreshSwitchEnabled);
        editor.putInt("keyUnits", AppConfig.getUnitsIndex());
        editor.putInt("keyPage", AppConfig.currentPagePos);
        editor.putString("keyLoc", FavouritesFragment.getSetLoc());
        editor.putInt("keyTimer", AppConfig.threadTimer);
        editor.putStringSet("keyFavs", FavouritesFragment.arrayListToSet());

        editor.commit();
    }

    private void loadSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
        int init = prefs.getInt("keyInit", 0);
        if (init == 0) return;

        AppConfig.isRefreshSwitchEnabled = prefs.getBoolean("keySwitch", false);
        AppConfig.setUnitsIndex(prefs.getInt("keyUnits", 0));
        AppConfig.currentPagePos = prefs.getInt("keyPage", 0);
        AppConfig.setCurrentLoc(prefs.getString("keyLoc", null));
        AppConfig.threadTimer = prefs.getInt("keyTimer", 5);
    }

    public ArrayList<String> recoverFavList() {
        SharedPreferences prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
        int init = prefs.getInt("keyInit", 0);
        if (init == 0) return new ArrayList<>();
        Set<String> sset = prefs.getStringSet("keyFavs", null);

        if (sset != null) {
            return new ArrayList<>(sset);
        } else {
            return new ArrayList<>();
        }

    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    private void prepareAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(RequestWeatherService.class);
    }

    public void getCityNameFromApi(String city, CityNameListener listener) {
        Log.i("CITYAPI", city);
        apiService.getCurrentWeather(AppConfig.getUnitsType(), city).enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                String cityName = response.body().getName();
                listener.onCityNameReceived(cityName);
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable throwable) {
                listener.onCityNameReceived("null");
            }
        });
    }

    public void getAPIData() {
        if (AppConfig.getCurrentLoc() == null) {
            writeToast("Localization is not set");
            return;
        }
        apiService.getCurrentWeather(AppConfig.getUnitsType(), AppConfig.getCurrentLoc()).enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
//                Log.i("Success", call.request().url() + " | hej " + String.valueOf(response.body().getName()));

                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiCurrent(response.body());
                CurrentWeatherJsonHolder holder = ((ScreenSlidePagerAdapter)pagerAdapter).getApiCurrent();
                saveOrUpdateJSON(holder);
                writeToast("Api called");
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable throwable) {
//                Log.i("Error current", Objects.requireNonNull(throwable.getCause()).getMessage() + " | " + throwable.getMessage());

                CurrentWeatherJsonHolder holder = loadCurrentJSON();
                if (holder.getTemp().equals("No data")) {
                    writeToast("There is no saved data for this localization");
                } else {
                    writeToast("JSON data loaded");
                }
                Log.i("HOLDER", holder.toString());
                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiCurrent(holder);
            }
        });

        apiService.getForecastWeather(AppConfig.getUnitsType(), AppConfig.getCurrentLoc()).enqueue(new Callback<ForecastWeatherData>() {
            @Override
            public void onResponse(Call<ForecastWeatherData> call, Response<ForecastWeatherData> response) {
//                Log.i("Success2", call.request().url() + " | hej " + String.valueOf(response.body().getCity().getName()));

                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiForecast(response.body());
                ForecastWeatherJsonHolder holder = ((ScreenSlidePagerAdapter)pagerAdapter).getApiForecast();
                Log.i("ONLINE SAVE FORECAST", holder.toString());
                saveOrUpdateJSON(holder);
            }

            @Override
            public void onFailure(Call<ForecastWeatherData> call, Throwable throwable) {
//                Log.i("Error forecast", throwable.getMessage());

                ForecastWeatherJsonHolder holder = loadForecastJSON();
                Log.i("HOLDER_FORECAST", holder.toString());

                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiForecast(holder);
            }
        });
    }

    private int findExistingLocationIndex(JSONArray jsonArray, String name) {
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

    public void saveOrUpdateJSON(CurrentWeatherJsonHolder data) {
        JSONArray jsonArray;
        try {
            String response = getJsonArrayFromFile(JSON_CURRENT);

//            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_CURRENT)));
            jsonArray = new JSONArray(response);
            Log.i("FILE_LOAD", "FOUND");
        } catch (IOException | JSONException e) {
            jsonArray = new JSONArray();
            Log.i("FILE_LOAD", "NOT FOUND");
        }

        JSONObject localization = createJsonObject(data);
        int existingIndex = findExistingLocationIndex(jsonArray, AppConfig.getCurrentLoc());

        if (existingIndex != -1) {
            jsonArray.remove(existingIndex);
        }

        jsonArray.put(localization);

        File file = new File(getApplicationContext().getFilesDir(), JSON_CURRENT);
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonArray.toString());
            bufferedWriter.close();
            Log.i("FILES", "SAVED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @NonNull
    private String getJsonArrayFromFile(String filename) throws IOException {
        File file = new File(getApplicationContext().getFilesDir(), filename);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        String responce = stringBuilder.toString();
        return responce;
    }

    public void saveOrUpdateJSON(ForecastWeatherJsonHolder data) {
        JSONArray jsonArray;
        try {
            String response = getJsonArrayFromFile(JSON_FORECAST);
            jsonArray = new JSONArray(response);

        } catch (IOException | JSONException e) {
            jsonArray = new JSONArray();
        }

        JSONObject localization = createJsonObject(data);
        int existingIndex = findExistingLocationIndex(jsonArray, AppConfig.getCurrentLoc());

        if (existingIndex != -1) {
            jsonArray.remove(existingIndex);
        }

        jsonArray.put(localization);

        File file = new File(getApplicationContext().getFilesDir(), JSON_FORECAST);
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonArray.toString());
            bufferedWriter.close();
            Log.i("FILES", "SAVED");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public CurrentWeatherJsonHolder dataToHolderTransfer(CurrentWeatherData data) {
//        String name = data.getName();
//        String coords = "Lat:" + data.getCoord().getLat()  + " Lon:" + data.getCoord().getLon();
//        String date = WeatherFragmentBasic.convertTime(data);
//        String icon = data.getWeather().get(0).getIcon();
//        String desc = data.getWeather().get(0).getDescription();
//        Integer temp = data.getMain().getTemp().intValue();
//        Integer degree = data.getWind().getDeg();
//        Double wind = data.getWind().getSpeed();
//        String humidity = data.getMain().getHumidity().toString();
//        Integer visibility = data.getVisibility();
//        String pressure = data.getMain().getPressure().toString();
//
//
//        CurrentWeatherJsonHolder holder = new CurrentWeatherJsonHolder(
//                name,
//                coords,
//                date,
//                icon,
//                desc,
//                temp,
//                degree,
//                wind,
//                humidity,
//                visibility,
//                pressure
//        );
//        return holder;
//    }

    public ForecastWeatherJsonHolder dataToHolderTransfer(ForecastWeatherData data) {
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

    public CurrentWeatherJsonHolder jsonToHolderTransfer(JSONObject data) {
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
            Log.i("HOLDER", holder.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return holder;
    }

    public ForecastWeatherJsonHolder jsonToHolderTransferForecast(JSONObject data) {
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
                Log.i("RECORD JSON", object.toString());
            }

            holder = new ForecastWeatherJsonHolder(name);
            holder.setRecords(records);

            Log.i("HOLDER", holder.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return holder;
    }

    public ForecastWeatherJsonHolder loadForecastJSON() {
        try {
            String response = getJsonArrayFromFile(JSON_FORECAST);
            JSONArray jsonArray = new JSONArray(response);
            Log.i("JSON_FINDING", FavouritesFragment.getSetLoc());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject locationJson = jsonArray.getJSONObject(i);
                String cityName = locationJson.getString(C_NAME);
                Log.i("JSON_LOAD", cityName);
                if (cityName.equals(AppConfig.getCurrentLoc())) {
                    return jsonToHolderTransferForecast(locationJson);
                }
            }
        } catch (IOException | JSONException e) {
            Log.i("EXCEPTION", "Here");
            e.printStackTrace();
        }
        return new ForecastWeatherJsonHolder();
    }

    public CurrentWeatherJsonHolder loadCurrentJSON() {
        try {
//            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FORECAST)));
            String response = getJsonArrayFromFile(JSON_CURRENT);
            JSONArray jsonArray = new JSONArray(response);
            Log.i("JSON_FINDING", FavouritesFragment.getSetLoc());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject locationJson = jsonArray.getJSONObject(i);
                String cityName = locationJson.getString(C_NAME);
                Log.i("JSON_LOAD", cityName);
                if (cityName.equals(AppConfig.getCurrentLoc())) {
                    return jsonToHolderTransfer(locationJson);
                }
            }
        } catch (IOException | JSONException e) {
            Log.i("EXCEPTION", "Here");
            e.printStackTrace();
        }
        return new CurrentWeatherJsonHolder();
    }


    private JSONObject createJsonObject(CurrentWeatherJsonHolder data) {
        JSONObject object = new JSONObject();
        try {
            object.put(C_NAME, data.getName());
            object.put(C_COORDS, data.getCoords());
            object.put(C_DATE, data.getDate());
            object.put(C_ICON, data.getIcon());
            Log.i("ICON CHECK", data.getIcon());
            object.put(C_DESC, data.getDesc());
            object.put(C_TEMP, data.getTemp());
            object.put(C_WIND_DEGREE, data.getWindDegree());
            object.put(C_WIND, data.getWind());
            object.put(C_HUMIDITY, data.getHumidity());
            object.put(C_VISIBILITY, data.getVisibility());
            object.put(C_PRESSURE, data.getPressure());
        } catch (JSONException e) {
            Log.i("EXCEPTION", "Here2");
            e.printStackTrace();
        }
        return object;
    }

    private JSONObject createJsonObject(ForecastWeatherJsonHolder data) {
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

    private void viewPagerHandle() {
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this, fragmentList);
        viewPager.setSaveFromParentEnabled(false);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pagerAdapter);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
        viewPager.setCurrentItem(AppConfig.currentPagePos);
    }

    private void createFragmentList() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new FavouritesFragment());
        fragmentList.add(new SettingsFragment());
    }

    private void setBottomNavViewListeners() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navWeather) {
                viewPager.setCurrentItem(0);
                AppConfig.currentPagePos = 0;
            }
            if (item.getItemId() == R.id.navFavourites) {
                viewPager.setCurrentItem(1);
                AppConfig.currentPagePos = 1;
            }
            if (item.getItemId() == R.id.navSettings) {
                viewPager.setCurrentItem(2);
                AppConfig.currentPagePos = 2;
            }

            return true;
        });
    }

    public static void writeToast(String s) {
        Toast toast= Toast.makeText(MainActivity.getMainActivity(), s, Toast.LENGTH_SHORT);
        toast.show();
    }

}