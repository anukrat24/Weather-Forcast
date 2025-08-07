package com.example.Weather.Forcast.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Weather.Forcast.Model.WeatherResponse;
import com.example.Weather.Forcast.Service.WeatherService;

@Controller
public class PageController {

    private final WeatherService weatherService;

    public PageController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // Loads the index.html at root path
    @GetMapping("/")
    public String showIndexPage() {
        return "index"; // Renders index.html
    }

    // Handles weather fetch and shows dashboard
    @GetMapping("/weather")
    public String getWeather(@RequestParam("city") String city, Model model) {
        WeatherResponse response = weatherService.getWeatherByCity(city);
        model.addAttribute("weather", response);
        return "dashboard"; // Renders dashboard.html
    }
}
