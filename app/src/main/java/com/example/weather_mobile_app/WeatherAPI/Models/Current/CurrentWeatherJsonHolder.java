package com.example.weather_mobile_app.WeatherAPI.Models.Current;

public class CurrentWeatherJsonHolder {
    public static final String C_NAME = "name";
    private String name;

    public static final String C_COORDS = "coords";
    private String coords;

    public static final String C_DATE = "date";
    private String date;

    public static final String C_ICON = "icon";
    private String icon;

    public static final String C_DESC = "desc";
    private String desc;

    public static final String C_TEMP = "temp";
    private String temp;

    public static final String C_WIND_DEGREE = "windDegree";
    private Integer windDegree;

    public static final String C_WIND = "wind";
    private String wind;

    public static final String C_HUMIDITY = "humidity";
    private String humidity;

    public static final String C_VISIBILITY = "visibility";
    private String visibility;

    public static final String C_PRESSURE = "pressure";
    private String pressure;

    public CurrentWeatherJsonHolder(String name, String coords, String date, String icon, String desc, String temp, Integer windDegree, String wind, String humidity, String visibility, String pressure) {
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

    public CurrentWeatherJsonHolder() {
        this.name = "-";
        this.coords = "-";
        this.date = "-";
        this.icon = "-";
        this.desc = "-";
        this.temp = "No data";
        this.windDegree = 0;
        this.wind = "-";
        this.humidity = "-";
        this.visibility = "-";
        this.pressure = "-";
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

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Integer getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(Integer windDegree) {
        this.windDegree = windDegree;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return "CurrentWeatherJsonHolder{" +
                "name='" + name + '\'' +
                ", coords='" + coords + '\'' +
                ", date='" + date + '\'' +
                ", icon='" + icon + '\'' +
                ", desc='" + desc + '\'' +
                ", temp=" + temp +
                ", windDegree=" + windDegree +
                ", wind=" + wind +
                ", humidity='" + humidity + '\'' +
                ", visibility=" + visibility +
                ", pressure='" + pressure + '\'' +
                '}';
    }
}
