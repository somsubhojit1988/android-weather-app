package com.example.weatherapplication.overview

data class CurrentWeather(
    val dt: Long,
    val temperature: Double,
    val apparentTemperature: Double,
    val description: String,
    val maxTemp: Double,
    val minTemp: Double,
    val icon: String
)

data class Forecast(
    val dt: Long,
    val description: String,
    val maxTemp: Double,
    val minTemp: Double,
    val icon: String
)
