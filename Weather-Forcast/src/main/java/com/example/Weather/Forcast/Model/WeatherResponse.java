package com.example.Weather.Forcast.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private String city;
    private String description;
    private double temperature;
    private double windSpeed;
    private int humidity;
}
