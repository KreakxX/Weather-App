package com.example.Weather_App;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherObject2 {

    private Integer dewPoint;

    private  Integer visibility;
}
