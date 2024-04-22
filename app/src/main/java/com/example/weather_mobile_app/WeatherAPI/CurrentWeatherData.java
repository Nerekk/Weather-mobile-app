package com.example.weather_mobile_app.WeatherAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentWeatherData {

    @SerializedName("coord")
    CoordClass coord;

    @SerializedName("weather")
    List<WeatherClass> weather;

    @SerializedName("main")
    MainClass main;

    @SerializedName("visibility")
    Integer visibility;

    @SerializedName("wind")
    WindClass wind;

    @SerializedName("name")
    String name;

    @SerializedName("cod")
    Integer cod;

    public CoordClass getCoord() {
        return coord;
    }

    public void setCoord(CoordClass coord) {
        this.coord = coord;
    }

    public List<WeatherClass> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherClass> weather) {
        this.weather = weather;
    }

    public MainClass getMain() {
        return main;
    }

    public void setMain(MainClass main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public WindClass getWind() {
        return wind;
    }

    public void setWind(WindClass wind) {
        this.wind = wind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    class CoordClass {
        Double lon, lat;

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }
    }
    class WeatherClass {
        Integer id;
        String main, description, icon;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
    class MainClass {
        Double temp, feels_like, temp_min, temp_max;
        Integer pressure, humidity;

        public Double getTemp() {
            return temp;
        }

        public void setTemp(Double temp) {
            this.temp = temp;
        }

        public Double getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(Double feels_like) {
            this.feels_like = feels_like;
        }

        public Double getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(Double temp_min) {
            this.temp_min = temp_min;
        }

        public Double getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(Double temp_max) {
            this.temp_max = temp_max;
        }

        public Integer getPressure() {
            return pressure;
        }

        public void setPressure(Integer pressure) {
            this.pressure = pressure;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }
    }
    class WindClass {
        Double speed;
        Integer deg;

        public Double getSpeed() {
            return speed;
        }

        public void setSpeed(Double speed) {
            this.speed = speed;
        }

        public Integer getDeg() {
            return deg;
        }

        public void setDeg(Integer deg) {
            this.deg = deg;
        }
    }
}
