package com.example.Weather_App;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/Weather")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService service;

    @GetMapping("/get/{location}")
    public WeatherObject getWeather(@PathVariable String location){
        return service.getWeather(location);
    }

    @GetMapping("/get/forecast/{location}")
    public Map<String, WeatherObject> getWeatherForecast(@PathVariable String location){
        return service.getWeatherForeCast(location);
    }

    @GetMapping("/get/weekly/min/max/temp/{location}")
    public List<WeeklyWeatherForecast> getTem(@PathVariable String location){
        return service.getMaxAndLowTemperatureOfWeek(location);
    }

}
