package com.example.weather_mobile_app.Utils;

import android.util.Log;

import com.example.weather_mobile_app.R;

public class WeatherIcons {
    public static final String IC_01D = "01d";
    public static final String IC_01N = "01n";
    public static final String IC_02D = "02d";
    public static final String IC_02N = "02n";
    public static final String IC_03D = "03d";
    public static final String IC_03N = "03n";
    public static final String IC_04D = "04d";
    public static final String IC_04N = "04n";
    public static final String IC_09D = "09d";
    public static final String IC_09N = "09n";
    public static final String IC_10D = "10d";
    public static final String IC_10N = "10n";
    public static final String IC_11D = "11d";
    public static final String IC_11N = "11n";
    public static final String IC_13D = "13d";
    public static final String IC_13N = "13n";
    public static final String IC_50D = "50d";
    public static final String IC_50N = "50n";

    public static int getIconResource(String id) {
//        Log.i("icon", id);
        switch (id) {
            case IC_01D:
                return R.drawable.ic_01d;
            case IC_01N:
                return R.drawable.ic_01n;
            case IC_02D:
                return R.drawable.ic_02d;
            case IC_02N:
                return R.drawable.ic_02n;
            case IC_03D:
                return R.drawable.ic_03d;
            case IC_03N:
                return R.drawable.ic_03n;
            case IC_04D:
                return R.drawable.ic_04d;
            case IC_04N:
                return R.drawable.ic_04n;
            case IC_09D:
                return R.drawable.ic_09d;
            case IC_09N:
                return R.drawable.ic_09n;
            case IC_10D:
                return R.drawable.ic_10d;
            case IC_10N:
                return R.drawable.ic_10n;
            case IC_11D:
                return R.drawable.ic_11d;
            case IC_11N:
                return R.drawable.ic_11n;
            case IC_13D:
                return R.drawable.ic_13d;
            case IC_13N:
                return R.drawable.ic_13n;
            case IC_50D:
                return R.drawable.ic_50d;
            case IC_50N:
                return R.drawable.ic_50n;
            default:
                return R.mipmap.ic_cloud;
        }
    }
}
