package com.example.Weather.Forcast.Controller;

import com.example.Weather.Forcast.Model.WeatherResponse;
import com.example.Weather.Forcast.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/weather")
public class UIController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public String showWeatherForm() {
        return "Weather"; // Displays weather.html
    }

    @PostMapping
    public String getWeather(@RequestParam("city") String city, Model model) {
        try {
            WeatherResponse weatherResponse = weatherService.getWeatherByCity(city);

            // ✅ Add individual current weather fields to the model
            model.addAttribute("city", weatherResponse.getCity());
            model.addAttribute("temperature", weatherResponse.getCurrentTemperature());
            model.addAttribute("humidity", weatherResponse.getCurrentHumidity()); // <- Added
            model.addAttribute("description", weatherResponse.getCurrentDescription());

            // ✅ Add the forecast list to the model
            model.addAttribute("forecast", weatherResponse.getForecast());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Unable to fetch weather data for: " + city);
        }
        return "Weather";
    }
}
