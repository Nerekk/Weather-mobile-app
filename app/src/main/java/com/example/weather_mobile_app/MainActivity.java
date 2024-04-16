package com.example.weather_mobile_app;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private FragmentStateAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createFragmentList();

        // Instantiate a ViewPager2 and a PagerAdapter.
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
        setBottomNavViewListeners();
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