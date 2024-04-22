package com.example.weather_mobile_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weather_mobile_app.Adapters.ScreenSlidePagerAdapter;
import com.example.weather_mobile_app.Fragments.ScreenSlideFragment;
import com.example.weather_mobile_app.WeatherAPI.Models.Current.CurrentWeatherData;
import com.example.weather_mobile_app.Interfaces.RequestWeatherService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private FragmentStateAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    RequestWeatherService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ONCREACTE", "AHA");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFragmentList();

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPagerHandle();
        setBottomNavViewListeners();

        Log.i("START", getPackageName());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(RequestWeatherService.class);
        getAPIData();
//        ((ScreenSlidePagerAdapter)pagerAdapter).updateApi(dataPack);
    }

    private void getAPIData() {
        apiService.getCurrentWeather("warsaw").enqueue(new Callback<CurrentWeatherData>() {
            @Override
            public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                Log.i("Success", call.request().url() + " | hej " + String.valueOf(response.body().getName()));
                ((ScreenSlidePagerAdapter)pagerAdapter).updateApiCurrent(response.body());
            }

            @Override
            public void onFailure(Call<CurrentWeatherData> call, Throwable throwable) {
                Log.i("Error", throwable.getMessage());
            }
        });
        Toast.makeText(this, "Api called", Toast.LENGTH_SHORT).show();
    }

    private void viewPagerHandle() {
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });
    }

    private void createFragmentList() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ScreenSlideFragment(R.layout.fragment_weather_main));
        fragmentList.add(new ScreenSlideFragment(R.layout.fragment_favourites_main));
        fragmentList.add(new ScreenSlideFragment(R.layout.fragment_settings_main));
    }

    private void setBottomNavViewListeners() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navWeather) viewPager.setCurrentItem(0);
            if (item.getItemId() == R.id.navFavourites) viewPager.setCurrentItem(1);
            if (item.getItemId() == R.id.navSettings) viewPager.setCurrentItem(2);

            return true;
        });
    }

    // obsluga stosu
//    @Override
//    public void onBackPressed() {
//        if (viewPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
//        }
//    }


}