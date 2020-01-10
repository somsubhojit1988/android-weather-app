package com.example.weatherapplication.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapplication.network.Current

@Entity(tableName = "current_weather_data")
data class Weather(
    @PrimaryKey var fetchDt: Long,
    var latitude: Double,
    var longitude: Double,
    @Embedded var currentWeather: Current
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