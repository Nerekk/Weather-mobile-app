package com.example.weather_mobile_app;

import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weather_mobile_app.Adapters.ScreenSlidePagerAdapter;
import com.example.weather_mobile_app.Fragments.FavouritesFragment;
import com.example.weather_mobile_app.Fragments.SettingsFragment;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragment;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragmentBasic;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;
import com.example.weather_mobile_app.Interfaces.RequestWeatherService;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        editor.putString("keyLoc", AppConfig.getCurrentLoc());
        editor.putInt("keyTimer", AppConfig.threadTimer);

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

    public void getAPIData() {
        if (AppConfig.getCurrentLoc() == null) {
            writeToast("Localization is not set");
            return;
        }
        apiService.getCurrentWeather(AppConfig.getUnitsType(), AppConfig.getCurrentLoc()).enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                Log.i("Success", call.request().url() + " | hej " + String.valueOf(response.body().getName()));
                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiCurrent(response.body());
                CurrentWeatherJsonHolder holder = dataToHolderTransfer(response.body());
                saveOrUpdateJSON(holder);
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable throwable) {
                Log.i("Error current", throwable.getMessage());
            }
        });

        apiService.getForecastWeather(AppConfig.getUnitsType(), AppConfig.getCurrentLoc()).enqueue(new Callback<ForecastWeatherData>() {
            @Override
            public void onResponse(Call<ForecastWeatherData> call, Response<ForecastWeatherData> response) {
                Log.i("Success2", call.request().url() + " | hej " + String.valueOf(response.body().getCity().getName()));
                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiForecast(response.body());
            }

            @Override
            public void onFailure(Call<ForecastWeatherData> call, Throwable throwable) {
                Log.i("Error forecast", throwable.getMessage());

            }
        });
        Toast.makeText(this, "Api called", Toast.LENGTH_SHORT).show();
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
            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_CURRENT)));
            jsonArray = new JSONArray(jsonContent);

        } catch (IOException | JSONException e) {
            jsonArray = new JSONArray();
        }

        JSONObject localization = createCurrentJsonObject(data);
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

    public void saveOrUpdateJSON(ForecastWeatherJsonHolder data) {
        JSONArray jsonArray;
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FORECAST)));
            jsonArray = new JSONArray(jsonContent);

        } catch (IOException | JSONException e) {
            jsonArray = new JSONArray();
        }

        JSONObject localization = createForecastJsonObject(data);
        int existingIndex = findExistingLocationIndex(jsonArray, AppConfig.getCurrentLoc());

        if (existingIndex != -1) {
            jsonArray.remove(existingIndex);
        }

        jsonArray.put(localization);

        try (FileWriter file = new FileWriter(JSON_FORECAST)) {
            file.write(jsonArray.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CurrentWeatherJsonHolder dataToHolderTransfer(CurrentWeatherData data) {
        String name = data.getName();
        String coords = "Lat:" + data.getCoord().getLat()  + " Lon:" + data.getCoord().getLon();
        String date = WeatherFragmentBasic.convertTime(data);
        String icon = data.getWeather().get(0).getIcon();
        String desc = data.getWeather().get(0).getDescription();
        Integer temp = data.getMain().getTemp().intValue();
        Integer degree = data.getWind().getDeg();
        Double wind = data.getWind().getSpeed();
        String humidity = data.getMain().getHumidity().toString();
        Integer visibility = data.getVisibility();
        String pressure = data.getMain().getPressure().toString();


        CurrentWeatherJsonHolder holder = new CurrentWeatherJsonHolder(
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
        return holder;
    }

    public CurrentWeatherJsonHolder jsonToHolderTransfer(JSONObject data) {
        CurrentWeatherJsonHolder holder;
        try {
            String name = data.getString(NAME);
            String coords = data.getString(COORDS);
            String date = data.getString(DATE);
            String icon = data.getString(ICON);
            String desc = data.getString(DESC);
            Integer temp = data.getInt(TEMP);
            Integer degree = data.getInt(WIND_DEGREE);
            Double wind = data.getDouble(WIND);
            String humidity = data.getString(HUMIDITY);
            Integer visibility = data.getInt(VISIBILITY);
            String pressure = data.getString(PRESSURE);

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

//    public ForecastWeatherJsonHolder loadForecastJSON() {
//
//    }
//
//    public CurrentWeatherJsonHolder loadCurrentJSON() {
//    }


    private JSONObject createCurrentJsonObject(CurrentWeatherJsonHolder data) {
        JSONObject object = new JSONObject();
        try {
            object.put(NAME, data.getName());
            object.put(COORDS, data.getCoords());
            object.put(DATE, data.getDate());
            object.put(ICON, data.getIcon());
            object.put(DESC, data.getDesc());
            object.put(TEMP, data.getTemp());
            object.put(WIND_DEGREE, data.getWindDegree());
            object.put(WIND, data.getWind());
            object.put(HUMIDITY, data.getHumidity());
            object.put(VISIBILITY, data.getVisibility());
            object.put(PRESSURE, data.getPressure());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private JSONObject createForecastJsonObject(ForecastWeatherJsonHolder data) {
        JSONObject object = new JSONObject();
        try {
            object.put("name", data.getName());

            JSONArray forecastsJson = new JSONArray();
            for (ForecastRecordJsonHolder record : data.getRecords()) {
                JSONObject forecastJson = new JSONObject();
                forecastJson.put("weekDay", record.getWeekDay());
                forecastJson.put("humidity", record.getHumidity());
                forecastJson.put("icon", record.getIcon());
                forecastJson.put("temp", record.getTemp());
                forecastsJson.put(forecastJson);
            }
            object.put("forecast", forecastsJson);
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