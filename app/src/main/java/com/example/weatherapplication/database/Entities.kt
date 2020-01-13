package com.example.weatherapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapplication.model.CurrentWeather
import com.example.weatherapplication.model.Forecast


@Entity(tableName = "current_weather_data")
data class WeatherEntity(
    @PrimaryKey var dt: Long,
    val latitude: Double,
    val longitude: Double,
    val summary: String,
    val icon: String,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val apparentTemperature: Double,
    val humidity: Double,
    val windSpeed: Double
)

fun WeatherEntity.asDomainModel(): CurrentWeather =
    CurrentWeather(
        dt = dt,
        temperature = temperature,
        apparentTemperature = apparentTemperature,
        description = summary,
        maxTemp = maxTemperature,
        minTemp = minTemperature,
        icon = icon
    )


@Entity(tableName = "forecasts")
data class ForecastEntity(
    @PrimaryKey val dt: Long,
    var summary: String,
    var icon: String,
    var sunriseTime: Long,
    var sunsetTime: Long,
    var temperatureHigh: Double,
    var temperatureLow: Double,
    var temperatureMin: Double,
    var temperatureMax: Double,
    var cloudCover: Double
)

fun ForecastEntity.asDomainModel() :Forecast = Forecast(
    dt = this.dt,
    description = this.summary,
    maxTemp = this.temperatureMax,
    minTemp = this.temperatureMin,
    icon = this.icon
)

fun List<ForecastEntity>.asDomainModel(): List<Forecast> =
    map {
        it.asDomainModel()
    }

