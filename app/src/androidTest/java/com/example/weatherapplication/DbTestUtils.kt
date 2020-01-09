package com.example.weatherapplication

import com.example.weatherapplication.database.Weather
import com.example.weatherapplication.network.Current

fun currentWeather() = Current(
    1578499949,
    "clear",
    "clear-day",
    59.21,
    59.21,
    0.26,
    6.61
)

fun testWeather() =
    Weather(
        System.currentTimeMillis(),
        34.00247,
        -84.18072,
        currentWeather()
    )
