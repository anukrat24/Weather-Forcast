package com.example.Weather.Forcast.Service;

import com.example.Weather.Forcast.Model.WeatherResponse;

public interface WeatherService {
    WeatherResponse getWeatherByCity(String city);
}
