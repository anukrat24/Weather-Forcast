package com.example.Weather.Forcast.Service;

import com.example.Weather.Forcast.Model.WeatherResponse;
import com.example.Weather.Forcast.Model.WeatherResponse.DailyForecast;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${weather.api.key}")
    private String apiKey;

    @Override
    public WeatherResponse getWeatherByCity(String city) {
        String currentUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                "&appid=" + apiKey + "&units=metric";

        String forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city +
                "&appid=" + apiKey + "&units=metric";

        try {
            // Get current weather
            ResponseEntity<String> currentResponse = restTemplate.getForEntity(currentUrl, String.class);
            JsonNode currentRoot = objectMapper.readTree(currentResponse.getBody());

            String cityName = currentRoot.path("name").asText();
            double currentTemp = currentRoot.path("main").path("temp").asDouble();
            int currentHumidity = currentRoot.path("main").path("humidity").asInt();
            System.out.println("Humidity from API: " + currentHumidity);
            String description = currentRoot.path("weather").get(0).path("description").asText();

            // Get forecast
            ResponseEntity<String> forecastResponse = restTemplate.getForEntity(forecastUrl, String.class);
            JsonNode forecastRoot = objectMapper.readTree(forecastResponse.getBody());

            Map<LocalDate, List<Double>> dailyTemps = new HashMap<>();
            Map<LocalDate, List<String>> dailyDescriptions = new HashMap<>();

            for (JsonNode node : forecastRoot.path("list")) {
                String dateTime = node.path("dt_txt").asText();
                String dateStr = dateTime.split(" ")[0];
                LocalDate forecastDate = LocalDate.parse(dateStr);

                if (forecastDate.isAfter(LocalDate.now()) && forecastDate.isBefore(LocalDate.now().plusDays(6))) {
                    double temp = node.path("main").path("temp").asDouble();
                    String dailyDesc = node.path("weather").get(0).path("description").asText();

                    dailyTemps.computeIfAbsent(forecastDate, k -> new ArrayList<>()).add(temp);
                    dailyDescriptions.computeIfAbsent(forecastDate, k -> new ArrayList<>()).add(dailyDesc);
                }
            }

            List<DailyForecast> forecastList = new ArrayList<>();
            for (LocalDate date : dailyTemps.keySet()) {
                List<Double> temps = dailyTemps.get(date);
                double min = temps.stream().min(Double::compare).orElse(0.0);
                double max = temps.stream().max(Double::compare).orElse(0.0);

                String mostFrequentDescription = dailyDescriptions.get(date).stream()
                        .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                        .entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("N/A");

                // Convert date to day name (e.g. "Monday")
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

                forecastList.add(new DailyForecast(dayName, min, max, mostFrequentDescription));
            }

            // Optional: sort by actual day order
            forecastList.sort(Comparator.comparing(f -> getDayOrder(f.getDay())));

            return new WeatherResponse(cityName, currentTemp, currentHumidity, description, forecastList);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse weather data", e);
        }
    }

    private int getDayOrder(String day) {
        switch (day.toLowerCase()) {
            case "monday": return 1;
            case "tuesday": return 2;
            case "wednesday": return 3;
            case "thursday": return 4;
            case "friday": return 5;
            case "saturday": return 6;
            case "sunday": return 7;
            default: return 8;
        }
    }
}
