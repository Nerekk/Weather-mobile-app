package com.example.weather_mobile_app;

public class AppConfig {
    public static boolean isRefreshSwitchEnabled = false;

    public static final int UNIT_D = 0;
    public static final int UNIT_M = 1;
    public static final int UNIT_I = 2;

    private static int unitsIndex = UNIT_D;

    public static int currentPagePos = 0;

    private static String currentLoc;

    public static int threadTimer = 1;

    public static final String DEFAULT = "default";
    public static final String METRIC = "metric";
    public static final String IMPERIAL = "imperial";

    public static final String DEGREES = "°";

    public static final String DEGREES_KELVIN = "°K";
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
        switch (unitsIndex) {
            case UNIT_D:
                return DEFAULT;
            case UNIT_M:
                return METRIC;
            case UNIT_I:
                return IMPERIAL;
        }
        return null;
    }

    public static String getDegreesType() {
        switch (unitsIndex) {
            case UNIT_D:
                return DEGREES_KELVIN;
            case UNIT_M:
                return DEGREES_CELSIUS;
            case UNIT_I:
                return DEGREES_FAHRENHEIT;
        }
        return null;
    }

    public static void setUnitsIndex(int unitsIndex) {
        if (unitsIndex < 0 || unitsIndex > 2) {
            AppConfig.unitsIndex = 0;
        } else {
            AppConfig.unitsIndex = unitsIndex;
        }
    }

    public static int getUnitsIndex() {
        return unitsIndex;
    }

    public static String getCurrentLoc() {
        return currentLoc;
    }

    public static void setCurrentLoc(String currentLoc) {
        AppConfig.currentLoc = currentLoc;
    }
}
