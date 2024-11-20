package com.example.Weather_App;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class WeatherService {

    public WeatherObject getWeather(String location)  {
        RestTemplate restTemplate = new RestTemplate();
        double longitude= 0;
        String timezone = "";
        double latitude = 0;
        int temperature = 0;
        double Humidity = 0;
        double WindSpeed = 0;
        double rain = 0;
        double rainPercent = 0;
        String weatherStatus = "";
        String geocodingURl = "https://geocoding-api.open-meteo.com/v1/search?name="+location+"&count=10&language=en&format=json";
        String GeocodingResponse = restTemplate.getForObject(geocodingURl,String.class);
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(GeocodingResponse);
            JsonNode resultsArray = root.path("results");

            if (resultsArray.isArray() && resultsArray.size() > 0) {
                JsonNode firstResult = resultsArray.get(0);

                // Zugriff auf die Latitude und Longitude
                 latitude = firstResult.path("latitude").asDouble();
                 longitude = firstResult.path("longitude").asDouble();
                 timezone = firstResult.path("timezone").asText();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        String WeatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone="+timezone;
        String WeatherResponse = restTemplate.getForObject(WeatherUrl,String.class);
        try{
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timezone));
            LocalDateTime now = zonedDateTime.toLocalDateTime();
            String currentHour = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0, 13);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(WeatherResponse);
            JsonNode current = root.path("current");

            /*
            JsonNode temperatureArray = current.path("temperature_2m");
            JsonNode HumidityArray = current.path("relative_humidity_2m");
            JsonNode WindSpeedArray = current.path("wind_speed_10m");
            JsonNode WeatherStatusArray = current.path("weather_code");
            JsonNode timeArray = current.path("time");
            */

             temperature = current.path("temperature_2m").asInt();
             Humidity = current.path("relative_humidity_2m").asInt();
             WindSpeed = current.path("wind_speed_10m").asInt();
             int weathercode = current.path("weather_code").asInt();
            weatherStatus = switch (weathercode) {
                case 0 -> "Clear";
                case 1, 2, 3 -> "Cloudy";
                case 45, 48 -> "Foggy";
                case 51, 53, 55, 56, 57 -> "Drizzle";
                case 61, 63, 65, 80, 81, 82 -> "Rainy";
                case 66, 67 -> "Freezing Rain";
                case 71, 73, 75, 85, 86 -> "Snowy";
                case 77 -> "Snow Grains";
                case 95, 96, 99 -> "Thunderstorm";
                default -> "Unknown";
            };
            /*
                 for(int i = 0; i<timeArray.size();i++){
                     String time = timeArray.get(i).asText().substring(0,13);
                     if(time.equals(currentHour)){
                         temperature = temperatureArray.get(i).asInt();
                         int weathercode = WeatherStatusArray.get(i).asInt();
                         weatherStatus = switch (weathercode) {
                             case 0 -> "Clear";
                             case 1, 2, 3 -> "Cloudy";
                             case 45, 48 -> "Foggy";
                             case 51, 53, 55, 56, 57 -> "Drizzle";
                             case 61, 63, 65, 80, 81, 82 -> "Rainy";
                             case 66, 67 -> "Freezing Rain";
                             case 71, 73, 75, 85, 86 -> "Snowy";
                             case 77 -> "Snow Grains";
                             case 95, 96, 99 -> "Thunderstorm";
                             default -> "Unknown";
                         };
                         Humidity = HumidityArray.get(i).asDouble();
                         WindSpeed = WindSpeedArray.get(i).asDouble();

                         break;
                     }
                 }*/
        }catch(
                Exception e
        ){
            e.printStackTrace();
        }
        return WeatherObject.builder()
                .WeatherStatus(weatherStatus)
                .Humidity(Humidity)
                .WindSpeed(WindSpeed)
                .Temperature(temperature)
                .location(location)
                .build();

    }

    public Map<String,WeatherObject> getWeatherForeCast(String location){
        RestTemplate restTemplate = new RestTemplate();
        double longitude= 0;
        String timezone = "";
        double latitude = 0;
        int temperature = 0;
        double Humidity = 0;
        double WindSpeed = 0;
        Map<String,WeatherObject>  forecast= new HashMap<>();
        String weatherStatus = "";
        String geocodingURl = "https://geocoding-api.open-meteo.com/v1/search?name="+location+"&count=10&language=en&format=json";
        String GeocodingResponse = restTemplate.getForObject(geocodingURl,String.class);
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(GeocodingResponse);
            JsonNode resultsArray = root.path("results");

            if (resultsArray.isArray() && resultsArray.size() > 0) {
                JsonNode firstResult = resultsArray.get(0);

                // Zugriff auf die Latitude und Longitude
                latitude = firstResult.path("latitude").asDouble();
                longitude = firstResult.path("longitude").asDouble();
                timezone = firstResult.path("timezone").asText();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        String WeatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m,precipitation_probability,precipitation&timezone="+timezone;
        String WeatherResponse = restTemplate.getForObject(WeatherUrl,String.class);
        try{
            ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timezone));
            LocalDateTime now = zonedDateTime.toLocalDateTime();
            String currentHour = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0, 13);
            

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(WeatherResponse);
            JsonNode hourly = root.path("hourly");
            JsonNode timeArray = hourly.path("time");
            JsonNode temperatureArray = hourly.path("temperature_2m");
            JsonNode HumidityArray = hourly.path("relative_humidity_2m");
            JsonNode WindSpeedArray = hourly.path("wind_speed_10m");
            JsonNode RainPercentArray = hourly.path("precipitation_probability");
            JsonNode Rain = hourly.path("precipitation");
            JsonNode WeatherStatusArray = hourly.path("weather_code");
            for(int i = 0; i<timeArray.size();i++){
                String time = timeArray.get(i).asText().substring(0,13);
                if(time.equals(currentHour)){
                    for(int j = 0 ;j <5; j++){
                        int weatherCode = WeatherStatusArray.get(i+j).asInt();
                        String hour = timeArray.get(i+j).asText().substring(11,13);
                        WeatherObject foreCastObject = WeatherObject.builder()
                                .location(location)
                                .WindSpeed(WindSpeedArray.get(i+j).asDouble())
                                .Rainprobability(RainPercentArray.get(i+j).asDouble())
                                .Rain(Rain.get(i+j).asDouble())
                                .Temperature(temperatureArray.get(i+j).asInt())
                                .Humidity(HumidityArray.get(i+j).asInt())
                                .WeatherStatus(switch (weatherCode) {
                                    case 0 -> "Clear";
                                    case 1, 2, 3 -> "Cloudy";
                                    case 45, 48 -> "Foggy";
                                    case 51, 53, 55, 56, 57 -> "Drizzle";
                                    case 61, 63, 65, 80, 81, 82 -> "Rainy";
                                    case 66, 67 -> "Freezing Rain";
                                    case 71, 73, 75, 85, 86 -> "Snowy";
                                    case 77 -> "Snow Grains";
                                    case 95, 96, 99 -> "Thunderstorm";
                                    default -> "Unknown";
                                })
                                .build();

                        forecast.put(hour,foreCastObject);
                    }

                    break;
                }
            }

        }catch(
                Exception e
        ){
            e.printStackTrace();
        }
        return forecast;
    }

    public List<WeeklyWeatherForecast> getMaxAndLowTemperatureOfWeek(String location){
        RestTemplate restTemplate = new RestTemplate();
        double longitude= 0;
        String timezone = "";
        double latitude = 0;
        List<WeeklyWeatherForecast> weeklyForecastList = new ArrayList<>();
        String geocodingURl = "https://geocoding-api.open-meteo.com/v1/search?name="+location+"&count=10&language=en&format=json";
        String GeocodingResponse = restTemplate.getForObject(geocodingURl,String.class);
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(GeocodingResponse);
            JsonNode resultsArray = root.path("results");

            if (resultsArray.isArray() && resultsArray.size() > 0) {
                JsonNode firstResult = resultsArray.get(0);

                latitude = firstResult.path("latitude").asDouble();
                longitude = firstResult.path("longitude").asDouble();
                timezone = firstResult.path("timezone").asText();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        String WeatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&daily=weather_code,temperature_2m_max,temperature_2m_min";
        String WeatherResponse = restTemplate.getForObject(WeatherUrl,String.class);
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(WeatherResponse);
            JsonNode daily = root.path("daily");
            JsonNode timeArray = daily.path("time");
            JsonNode MaxTemp  = daily.path("temperature_2m_max" );
            JsonNode MinTemp  = daily.path("temperature_2m_min");
            JsonNode WeatherCodes = daily.path("weather_code");
            for(int i = 0 ; i<timeArray.size();i++){
                int Weathercode = WeatherCodes.get(i).asInt();
                String time = timeArray.get(i).asText();
                LocalDate date = LocalDate.parse(time, DateTimeFormatter.ISO_LOCAL_DATE);
                String dayOfTheWeek = date.getDayOfWeek().toString().toLowerCase();
                String capitalizedDayOfTheWeek = dayOfTheWeek.substring(0, 1).toUpperCase() + dayOfTheWeek.substring(1);
                LocalDate date1 = LocalDate.now();
                String Today =date1.getDayOfWeek().toString().toLowerCase();
                String TodayCapitalize = Today.substring(0, 1).toUpperCase() + Today.substring(1);
                if(capitalizedDayOfTheWeek.equals(TodayCapitalize)){
                    capitalizedDayOfTheWeek ="Today";
                }
                weeklyForecastList.add( WeeklyWeatherForecast.builder()
                        .Date(capitalizedDayOfTheWeek)
                        .maxTemp(MaxTemp.get(i).asInt())
                        .minTemp(MinTemp.get(i).asInt())
                        .WeatherCode(switch (Weathercode) {
                            case 0 -> "Clear";
                            case 1, 2, 3 -> "Cloudy";
                            case 45, 48 -> "Foggy";
                            case 51, 53, 55, 56, 57 -> "Drizzle";
                            case 61, 63, 65, 80, 81, 82 -> "Rainy";
                            case 66, 67 -> "Freezing Rain";
                            case 71, 73, 75, 85, 86 -> "Snowy";
                            case 77 -> "Snow Grains";
                            case 95, 96, 99 -> "Thunderstorm";
                            default -> "Unknown";
                        })

                        .build());
            }

        }catch(
                Exception e
        ){
            e.printStackTrace();
        }
        return weeklyForecastList;
    }

}
