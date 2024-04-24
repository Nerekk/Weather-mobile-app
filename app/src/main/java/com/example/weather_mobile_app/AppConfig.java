package com.example.weather_mobile_app;

public class AppConfig {
    public static boolean isRefreshSwitchEnabled;
    public static boolean isUnitsSwitchEnabled;

    public static final String IMPERIAL = "imperial";
    public static final String METRIC = "metric";
    public static final String DEGREES = "°";
    public static final String DEGREES_CELSIUS = "°C";
    public static final String DEGREES_FAHRENHEIT = "°F";

    public static int getRefreshTime() {
        if (isRefreshSwitchEnabled) {
            return 900;
        } else {
            return 5;
        }
    }

    public static String getUnitsType() {
        if (isUnitsSwitchEnabled) {
            return IMPERIAL;
        } else {
            return METRIC;
        }
    }

    public static String getDegreesType() {
        if (isUnitsSwitchEnabled) {
            return DEGREES_FAHRENHEIT;
        } else {
            return DEGREES_CELSIUS;
        }
    }
}
