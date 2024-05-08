package com.example.weather_mobile_app.WeatherAPI.Models.Forecast;

public class ForecastRecordJsonHolder {

    public final static String F_WEEK_DAY = "weekDay";
    private String weekDay;

    public final static String F_HUMIDITY = "humidity";
    private String humidity;

    public final static String F_ICON = "icon";
    private String icon;

    public final static String F_TEMP = "temp";
    private String temp;

    public ForecastRecordJsonHolder(String weekDay, String humidity, String icon, String temp) {
        this.weekDay = weekDay;
        this.humidity = humidity;
        this.icon = icon;
        this.temp = temp;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "ForecastRecordJsonHolder{" +
                "weekDay='" + weekDay + '\'' +
                ", humidity='" + humidity + '\'' +
                ", icon='" + icon + '\'' +
                ", temp='" + temp + '\'' +
                '}';
    }
}
