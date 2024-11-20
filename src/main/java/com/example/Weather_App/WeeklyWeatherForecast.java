package com.example.Weather_App;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WeeklyWeatherForecast {
    private String Date;
    private Integer maxTemp;
    private Integer minTemp;
    private String WeatherCode;

}
