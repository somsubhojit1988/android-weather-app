package com.example.weatherapplication

import android.content.Context
import com.example.weatherapplication.network.WeatherResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


fun getWeatherResponse(context: Context) =
    parseWeatherResponse(readJsonFileToString(context, R.raw.weather_forecast))


fun parseWeatherResponse(jsonString: String): WeatherResponse? =
    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter(WeatherResponse::class.java).fromJson(jsonString)


fun readJsonFileToString(context: Context, resId: Int): String =
    context.resources.openRawResource(resId).bufferedReader().use {
        it.readText()
    }