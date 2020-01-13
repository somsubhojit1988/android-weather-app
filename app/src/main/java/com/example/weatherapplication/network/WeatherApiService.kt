package com.example.weatherapplication.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.darksky.net/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL).build()

interface WeatherApiService {
    @GET("forecast/{appId}/{lat},{lon}")
    suspend fun getWeatherForecastAsync(
        @Path("appId") appId: String,
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Query("exclude") excludeList: List<String> = listOf("minutely", "hourly"),
        @Query("units") units: String = "si" // si: SI units; us: Imperial Units
    ): WeatherResponse
}

object WeatherForecastService {
    val weatherReportService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}