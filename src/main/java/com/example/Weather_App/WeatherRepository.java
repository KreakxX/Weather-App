package com.example.Weather_App;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherObject, Integer> {
}
