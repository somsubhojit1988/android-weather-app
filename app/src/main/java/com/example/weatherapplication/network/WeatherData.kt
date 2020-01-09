package com.example.weatherapplication.network

import com.squareup.moshi.Json



data class Current(
    @Json(name = "time") val time: Long,
    @Json(name = "summary") val summary: String,
    @Json(name = "icon") val icon: String,
    @Json(name = "temperature") val temperature: Double,
    val apparentTemperature: Double,
    @Json(name = "humidity") val humidity: Double,
    @Json(name = "windSpeed") val windSpeed: Double
)

data class DayForecast(
    val time: Long,
    val summary: String,
    val icon: String,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val temperatureHigh: Double,
    val temperatureLow: Double,
    val temperatureMin: Double,
    val temperatureMax: Double,
    val cloudCover: Double
)

class DailyForecastList (
    val summary: String,
    val icon: String,
    val data: List<DayForecast>
)

data class WeatherResponse (
    val latitude: Double,
    val longitude: Double,
    val currently: Current,
    val daily: DailyForecastList,
    val offset: Int
)