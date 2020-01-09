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
data class Forecast(
    @PrimaryKey val dt: Long,
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