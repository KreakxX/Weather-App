"use client";

import { Card, CardContent } from "../components/ui/card";
import {
  Activity,
  Cloud,
  CloudFog,
  CloudRain,
  CloudRainWind,
  Droplet,
  Droplets,
  LocateFixedIcon,
  LocateIcon,
  LocateOffIcon,
  MapPin,
  Menu,
  Search,
  Snowflake,
  Sun,
  Wind,
} from "lucide-react";
import { Button } from "../components/ui/button";
import { useEffect, useState } from "react";
import {
  getWeatherData,
  GetWeatherForecast,
  getWeeklyTempForecast,
} from "../Api";
import { Weather } from "@/Weather";
import { Input } from "../components/ui/input";
import { WeeklyForecast } from "@/WeeklyForecast";
import { ScrollArea } from "../components/ui/scroll-area";
import {
  ChartConfig,
  ChartContainer,
  ChartTooltip,
  ChartTooltipContent,
} from "../components/ui/chart";
import { Area, AreaChart, CartesianGrid, XAxis, YAxis } from "recharts";

export default function WeatherComponent() {
  const [forecast, setForecast] = useState<Record<string, Weather>>({});
  const [location, setLocation] = useState("Berlin");
  const [weather, setWeather] = useState<Weather>();
  const [weeklyTempForecast, setWeeklyTempForecast] = useState<
    WeeklyForecast[]
  >([]);
  const [WeeklyWeatherCodeForecast, setWeeklyWeatherCodeForecast] = useState();
  const currentHour = new Date().getHours();

  const sortedForecast = Object.entries(forecast)
    .sort(([hourA], [hourB]) => parseInt(hourA, 10) - parseInt(hourB, 10))
    .sort(([hourA], [hourB]) => {
      const hourANum = parseInt(hourA, 10);
      const hourBNum = parseInt(hourB, 10);

      const isAInFuture = hourANum >= currentHour;
      const isBInFuture = hourBNum >= currentHour;

      if (isAInFuture === isBInFuture) {
        return 0;
      }

      return isAInFuture ? -1 : 1;
    });
  const chartData = Object.entries(forecast).map(([hour, weather]) => ({
    hour,
    rain: weather.rain,
  }));
  const getWeatherInformation = async () => {
    try {
      const response = await getWeatherData(location);
      setWeather(response);
    } catch (error) {
      console.log(error);
      throw error;
    }
  };

  const getWeatherForecast = async () => {
    try {
      const response = await GetWeatherForecast(location);
      setForecast(response);
    } catch (error) {
      console.log(error);
      throw error;
    }
  };

  const GETWeeklyTempForecast = async () => {
    try {
      const response = await getWeeklyTempForecast(location);
      console.log(response);
      setWeeklyTempForecast(response);
    } catch (error) {
      console.log(error);
      throw error;
    }
  };
  useEffect(() => {
    getWeatherInformation();
    getWeatherForecast();
    GETWeeklyTempForecast();
  }, []);
  let mainBgColor = "bg-[#3a4265]";
  let cardBgColor = "bg-[#4a547d]";

  if (
    weather?.weatherStatus?.toLowerCase() === "rainy" ||
    weather?.weatherStatus?.toLowerCase() === "drizzle"
  ) {
    mainBgColor = "bg-gradient-to-b from-[#7CB7B7] to-[#5B98A8]";
    cardBgColor = "bg-[#5B98A8]/80";
  } else if (weather?.weatherStatus?.toLowerCase() === "clear") {
    mainBgColor = "bg-gradient-to-b from-[#87CEEB] to-[#4682B4]";
    cardBgColor = "bg-[#4682B4]/80";
  } else if (weather?.weatherStatus?.toLowerCase() === "snowy") {
    mainBgColor = "bg-gradient-to-b from-[#A6B9C7] to-[#7F9EB9]";
    cardBgColor = "bg-[#7F9EB9]/80";
  }

  const chartConfig = {
    desktop: {
      label: "Desktop",
      color: "hsl(var(--chart-1))",
      icon: Activity,
    },
  } satisfies ChartConfig;

  return (
    <Card
      className={`mx-auto text-white w-[700px] h-[950px] transition-colors duration-500 ${mainBgColor} border-none`}
    >
      <CardContent className="p-8">
        <div className="flex items-center justify-between mb-8">
          <div className="flex items-center gap-3">
            <MapPin className="h-7 w-7" />
            <Input
              className="border-white"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
            />
            <Search
              className="relative left-2"
              onClick={() => {
                getWeatherInformation();
                getWeatherForecast();
                GETWeeklyTempForecast();
              }}
            />
          </div>
        </div>
        <div className="mb-8 text-center relative bottom-4">
          <div className="relative mx-auto flex justify-center items-center">
            <div className="text-8xl font-light">{weather?.temperature}°</div>

            {weather?.weatherStatus === "Cloudy" && (
              <div
                style={{ right: "50px" }}
                className="absolute flex items-center gap-x-2"
              >
                <Cloud
                  style={{ height: "70px", width: "70px" }}
                  className="h-8 w-8 fill-white opacity-80 animate-cloud-1"
                />
                <Cloud
                  style={{ height: "60px", width: "60px" }}
                  className="h-7 w-7 fill-white opacity-80 animate-cloud-2"
                />
              </div>
            )}
          </div>
          <div className="text-3xl mb-2">{weather?.weatherStatus}</div>
          <div className="flex justify-center gap-8 mb-8 relative top-4 right-2">
            <div className="flex items-center gap-2">
              <Droplets className="h-5 w-5 text-gray-300" />
              <div>
                <div className="text-sm text-gray-300">Humidity</div>
                <div className="text-mg">{weather?.humidity} %</div>
              </div>
            </div>
            <div className="flex items-center gap-2">
              <Wind className="h-5 w-5 text-gray-300" />
              <div>
                <div className="text-sm text-gray-300">Wind Speed</div>
                <div className="text-mg">{weather?.windSpeed} km/h</div>
              </div>
            </div>
          </div>
        </div>

        <Card
          className={`grid grid-cols-5 gap-3 text-center relative  left-14 h-[145px] w-[550px] border-none pt-3 transition-colors duration-500 ${cardBgColor}`}
        >
          {sortedForecast.map(([hour, weather], index) => (
            <div key={index} className="flex flex-col items-center">
              <div className="text-base text-gray-300 mb-2">{hour}:00</div>
              {weather?.weatherStatus === "Cloudy" && (
                <Cloud className="h-6 w-6 text-gray-300 fill-gray-300" />
              )}
              {weather?.weatherStatus === "Rainy" && (
                <CloudRainWind className="h-6 w-6 text-gray-300 fill-gray-300" />
              )}
              {weather?.weatherStatus === "Clear" && (
                <Sun className="h-6 w-6 text-yellow-300 fill-yellow-300" />
              )}
              {weather?.weatherStatus === "Snowy" && (
                <Snowflake className="h-6 w-6 text-blue-300" />
              )}
              {weather?.weatherStatus === "Drizzle" && (
                <CloudRainWind className="h-6 w-6 text-gray-300 fill-gray-300" />
              )}
              {weather?.weatherStatus === "Foggy" && (
                <CloudFog className="h-6 w-6 text-gray-300 fill-gray-300" />
              )}
              <div className="text-lg mb-1 text-white">
                {weather.temperature}°
              </div>
              <div className="text-sm text-gray-300 flex items-center relative top-3">
                <Droplet className="text-blue-300 fill-blue-300 h-4 w-4 mr-1" />{" "}
                {weather.rainprobability}%
              </div>
            </div>
          ))}
        </Card>
        <Card
          className={`text-center relative top-3 left-14 h-[115px] w-[550px] border-none pt-3 transition-colors duration-500 ${cardBgColor}`}
        >
          <ChartContainer
            className="h-[100px] w-[550px] relative right-2"
            config={chartConfig}
          >
            <AreaChart
              accessibilityLayer
              data={chartData}
              margin={{
                left: 12,
                right: 12,
              }}
            >
              <CartesianGrid vertical={false} horizontal={false} />
              <XAxis
                dataKey="hour"
                axisLine={false}
                tickMargin={8}
                tickLine={false}
                tickFormatter={(value) => value}
                style={{ fill: "#FFFFFF" }}
              />
              <YAxis
                dataKey="rain"
                axisLine={false}
                tickMargin={8}
                tickLine={false}
                tickFormatter={(value) => `${value} mm`}
                style={{ fill: "#FFFFFF" }}
                width={80}
              />

              <Area
                dataKey="rain"
                type="step"
                fill="#D3D3D3"
                fillOpacity={0.4}
                stroke="#D3D3D3"
              />
            </AreaChart>
          </ChartContainer>
        </Card>
        <ScrollArea
          className="h-[300px] w-[300px] mx-auto "
          style={{ top: "30px" }}
        >
          <Card
            className={`border-none transition-colors duration-500 ${cardBgColor}`}
          >
            <div className="space-y-4 p-4">
              {weeklyTempForecast.map((forecast, index) => (
                <div key={index} className="flex items-center justify-between">
                  <div className="w-1/4 text-left text-white">
                    {forecast.date}
                  </div>
                  <div className="flex items-center space-x-4">
                    {forecast?.weatherCode === "Cloudy" && (
                      <Cloud className="h-6 w-6 text-gray-300 fill-gray-300" />
                    )}
                    {forecast?.weatherCode === "Rainy" && (
                      <CloudRainWind className="h-6 w-6 text-gray-300 fill-gray-300" />
                    )}
                    {forecast?.weatherCode === "Clear" && (
                      <Sun className="h-6 w-6 text-yellow-300 fill-gray-300" />
                    )}
                    {forecast?.weatherCode === "Snowy" && (
                      <Snowflake className="h-6 w-6 text-blue-300" />
                    )}
                    {forecast?.weatherCode === "Drizzle" && (
                      <CloudRainWind className="h-6 w-6 text-gray-300 fill-gray-300" />
                    )}
                    {forecast?.weatherCode === "Foggy" && (
                      <CloudFog className="h-6 w-6 text-gray-300 fill-gray-300" />
                    )}
                    <div className="text-sm">
                      <span className="font-medium text-white">
                        {forecast.maxTemp}°
                      </span>
                      <span className="text-gray-400 text-white">
                        {" "}
                        / {forecast.minTemp}°
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </ScrollArea>
      </CardContent>
    </Card>
  );
}
