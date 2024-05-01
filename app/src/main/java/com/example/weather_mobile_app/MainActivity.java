package com.example.weather_mobile_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weather_mobile_app.Adapters.ScreenSlidePagerAdapter;
import com.example.weather_mobile_app.Fragments.FavouritesFragment;
import com.example.weather_mobile_app.Fragments.SettingsFragment;
import com.example.weather_mobile_app.Fragments.Weather.WeatherFragment;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;
import com.example.weather_mobile_app.Interfaces.RequestWeatherService;
import com.example.weather_mobile_app.WeatherAPI.Models.Forecast.ForecastWeatherData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String SP_FILE = "MyPrefsFile";

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

    public void loadCurrentJSON() {

    }

    public void saveCurrentJSON() {

    }

    public void loadForecastJSON() {

    }

    public void saveForecastJSON() {

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