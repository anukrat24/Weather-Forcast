package com.example.Weather.Forcast.Service;

import com.example.Weather.Forcast.Model.WeatherResponse;
import com.example.Weather.Forcast.Service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${weather.api.key}")
    private String apiKey;

    @Override
    public WeatherResponse getWeatherByCity(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                "&appid=" + apiKey + "&units=metric";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());

            String cityName = root.path("name").asText();
            String description = root.path("weather").get(0).path("description").asText();
            double temperature = root.path("main").path("temp").asDouble();
            double windSpeed = root.path("wind").path("speed").asDouble();
            int humidity = root.path("main").path("humidity").asInt();

            return new WeatherResponse(cityName, description, temperature, windSpeed, humidity);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather data", e);
        }
    }
}
