package com.example.weatherapplication.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.darksky.net/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL).build()

interface WeatherApiService {

    @GET("forecast/{appId}/{lat},{lon}")
    fun getWeatherForecast(
        @Path("appId") appId: String,
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Query("exclude") excludeList: List<String> = listOf("minutely","hourly"),
        @Query("units") units: String = "si" // si: SI units; us: Imperial Units
    ): Deferred<WeatherResponse>
}

object WeatherForecastService {
    val srvc: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}