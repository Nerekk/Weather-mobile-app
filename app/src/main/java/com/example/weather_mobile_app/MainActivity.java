package com.example.weather_mobile_app;

import static com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherJsonHolder.*;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastRecordJsonHolder.*;
import static com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherJsonHolder.*;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weather_mobile_app.Adapters.ScreenSlidePagerAdapter;
import com.example.weather_mobile_app.Fragments.FavouritesFragment;
import com.example.weather_mobile_app.Fragments.SettingsFragment;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragment;
import com.example.weather_mobile_app.Interfaces.CityNameListener;
import com.example.weather_mobile_app.Utils.DataHelper;
import com.example.weather_mobile_app.Utils.TimerThread;
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
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String SP_FILE = "MyPrefsFile";
    public static final String JSON_CURRENT = "SavedCurrentLocations.json";
    public static final String JSON_FORECAST = "SavedForecastLocations.json";

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private FragmentStateAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    public static MainActivity mainActivity;

    private TimerThread thread;

    RequestWeatherService apiService;
    Configuration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppConfig.PATH_DIR = getApplicationContext().getFilesDir();
        configuration = getResources().getConfiguration();

        loadSharedPreferences();

        createFragmentList();

        viewPagerHandle();

        if (configuration.smallestScreenWidthDp < 460)
            setBottomNavViewListeners();

        mainActivity = this;

        prepareAPIService();
    }

    @Override
    protected void onResume() {
        super.onResume();

        thread = new TimerThread();
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        thread.interrupt();
        saveSharedPreferences();
    }

    public void restartThread() {
        if (thread != null) {
            thread.interrupt();
            AppConfig.prepareTimer();
            thread = new TimerThread();
            thread.start();
        }
    }

    public CurrentWeatherJsonHolder getCurrentDataOffline() {
        CurrentWeatherJsonHolder holder1 = DataHelper.loadCurrentJSON();

        return holder1;
    }

    public ForecastWeatherJsonHolder getForecastDataOffline() {
        ForecastWeatherJsonHolder holder2 = DataHelper.loadForecastJSON();
        return holder2;
    }

    private void saveSharedPreferences() {
        SharedPreferences.Editor editor = getSharedPreferences(SP_FILE, MODE_PRIVATE).edit();

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
        apiService.getCurrentWeather(AppConfig.getUnitsType(), city).enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                if (response.isSuccessful()){
                    listener.onCityNameReceived(response.body().getName());
                } else {
                    listener.onCityNameReceived("0");
                }
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable throwable) {
                listener.onCityNameReceived("0");
            }
        });
    }

    public void getAPIData() {
        if (AppConfig.getCurrentLoc() == null) {
            writeToast("Localization is not set");
            AppConfig.setCurrentLoc("Not set");
        }
        apiService.getCurrentWeather(AppConfig.getUnitsType(), AppConfig.getCurrentLoc()).enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                if (response.isSuccessful()) {
                    ((ScreenSlidePagerAdapter)pagerAdapter).updateApiCurrent(response.body());
                    CurrentWeatherJsonHolder holder = ((ScreenSlidePagerAdapter)pagerAdapter).getApiCurrent();
                    DataHelper.saveOrUpdateJSON(holder);
                    writeToast("Api called");
                } else {
                    ((ScreenSlidePagerAdapter)pagerAdapter).updateApiCurrent(new CurrentWeatherJsonHolder());
                }

            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable throwable) {
                CurrentWeatherJsonHolder holder = DataHelper.loadCurrentJSON();
                if (holder.getTemp().equals("No data")) {
                    writeToast("There is no saved data for this localization");
                } else {
                    writeToast("JSON data loaded");
                }
                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiCurrent(holder);
            }
        });

        apiService.getForecastWeather(AppConfig.getUnitsType(), AppConfig.getCurrentLoc()).enqueue(new Callback<ForecastWeatherData>() {
            @Override
            public void onResponse(Call<ForecastWeatherData> call, Response<ForecastWeatherData> response) {
                if (response.isSuccessful()) {
                    ((ScreenSlidePagerAdapter)pagerAdapter).updateApiForecast(response.body());
                    ForecastWeatherJsonHolder holder = ((ScreenSlidePagerAdapter)pagerAdapter).getApiForecast();
                    DataHelper.saveOrUpdateJSON(holder);
                } else {
                    ((ScreenSlidePagerAdapter)pagerAdapter).updateApiForecast(new ForecastWeatherJsonHolder());
                }
            }

            @Override
            public void onFailure(Call<ForecastWeatherData> call, Throwable throwable) {
                ForecastWeatherJsonHolder holder = DataHelper.loadForecastJSON();
                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiForecast(holder);
            }
        });
    }


    private void viewPagerHandle() {
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this, fragmentList);
        viewPager.setSaveFromParentEnabled(false);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pagerAdapter);

        if (configuration.smallestScreenWidthDp < 460) {
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
    }

    private void createFragmentList() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new WeatherFragment());
        if (configuration.smallestScreenWidthDp < 460) {
            fragmentList.add(new FavouritesFragment());
            fragmentList.add(new SettingsFragment());
        }
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