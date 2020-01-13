package com.example.weatherapplication.network

import com.example.weatherapplication.database.ForecastEntity
import com.example.weatherapplication.database.WeatherEntity
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

class DailyForecastList(
    val summary: String,
    val icon: String,
    val data: List<DayForecast>
)

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val currently: Current,
    val daily: DailyForecastList,
    val offset: Int
)

fun WeatherResponse.asWeatherEntity(): WeatherEntity =
    WeatherEntity(
        dt = currently.time,
        latitude = latitude,
        longitude = longitude,
        summary = currently.summary,
        icon = currently.icon,
        temperature = currently.temperature,
        maxTemperature = daily.data[0].temperatureMax,
        minTemperature = daily.data[0].temperatureMin,
        apparentTemperature = currently.apparentTemperature,
        humidity = currently.humidity,
        windSpeed = currently.windSpeed
    )

fun WeatherResponse.asForecastEntity(): List<ForecastEntity> =
    daily.data.subList(1, daily.data.size).map {
        ForecastEntity(
            dt = it.time,
            summary = it.summary,
            icon = it.icon,
            sunriseTime = it.sunriseTime,
            sunsetTime = it.sunsetTime,
            temperatureHigh = it.temperatureHigh,
            temperatureLow = it.temperatureLow,
            temperatureMin = it.temperatureMin,
            temperatureMax = it.temperatureMax,
            cloudCover = it.cloudCover
        )
    }