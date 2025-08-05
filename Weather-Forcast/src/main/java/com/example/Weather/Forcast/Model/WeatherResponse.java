package com.example.Weather.Forcast.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private String city;
    private double currentTemperature;
    private int currentHumidity;
    private String currentDescription;
    private List<DailyForecast> forecast;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyForecast {
        private String day;
        private double minTemp;
        private double maxTemp;
        private String description;
    }
}
