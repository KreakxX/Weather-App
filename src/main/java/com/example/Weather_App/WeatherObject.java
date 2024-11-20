package com.example.Weather_App;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherObject {
    @Id
    @GeneratedValue
    private Integer id;

    private String location;

    private int Temperature;

    private double WindSpeed;

    private double Humidity;

    private String WeatherStatus;

    private double Rain;

    private double Rainprobability;

    // TODO Temperature for Time Zone
}
