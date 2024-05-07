package com.example.weather_mobile_app.WeatherAPI.Models.Current;

public class CurrentWeatherJsonHolder {
    public static final String NAME = "name";
    private String name;

    public static final String COORDS = "coords";
    private String coords;

    public static final String DATE = "date";
    private String date;

    public static final String ICON = "icon";
    private String icon;

    public static final String DESC = "desc";
    private String desc;

    public static final String TEMP = "temp";
    private Integer temp;

    public static final String WIND_DEGREE = "windDegree";
    private Integer windDegree;

    public static final String WIND = "wind";
    private Double wind;

    public static final String HUMIDITY = "humidity";
    private String humidity;

    public static final String VISIBILITY = "visibility";
    private Integer visibility;

    public static final String PRESSURE = "pressure";
    private String pressure;

    public CurrentWeatherJsonHolder(String name, String coords, String date, String icon, String desc, Integer temp, Integer windDegree, Double wind, String humidity, Integer visibility, String pressure) {
        this.name = name;
        this.coords = coords;
        this.date = date;
        this.icon = icon;
        this.desc = desc;
        this.temp = temp;
        this.windDegree = windDegree;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.pressure = pressure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(Integer windDegree) {
        this.windDegree = windDegree;
    }

    public Double getWind() {
        return wind;
    }

    public void setWind(Double wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }
}
