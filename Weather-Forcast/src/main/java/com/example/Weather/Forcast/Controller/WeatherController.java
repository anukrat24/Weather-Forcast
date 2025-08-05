package com.example.Weather.Forcast.Controller;

import com.example.Weather.Forcast.Model.WeatherResponse;
import com.example.Weather.Forcast.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{city}")
    public WeatherResponse getWeather(@PathVariable String city) {
        return weatherService.getWeatherByCity(city); // method name should match
    }

}
