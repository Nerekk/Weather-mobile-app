package com.example.weather_mobile_app.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {

    List<Fragment> fragmentList;

    public ScreenSlidePagerAdapter(FragmentActivity fa, List<Fragment> fragmentList) {
        super(fa);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return fragmentList.get(0);
            case 1:
                return fragmentList.get(1);
            case 2:
                return fragmentList.get(2);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
