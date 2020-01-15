package com.example.weatherapplication.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.darksky.net/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


interface WeatherApiService {
    @GET("forecast/{appId}/{lat},{lon}")
    // implicit support of co-routines (no need to add call adapter factory to Retrofit)
    suspend fun getWeatherForecastAsync(
        @Path("appId") appId: String,
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Query("exclude") excludeList: List<String> = listOf("minutely", "hourly"),
        @Query("units") units: String = "si" // si: SI units; us: Imperial Units
    ): WeatherResponse

    @GET("forecast/{appId}/{lat},{lon}")
    fun getWeatherForecast(
        @Path("appId") appId: String,
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Query("exclude") excludeList: List<String> = listOf("minutely", "hourly"),
        @Query("units") units: String = "si" // si: SI units; us: Imperial Units
    ): Single<WeatherResponse>
}

private interface BaseRetrofitBuilder {
    val builder: Retrofit.Builder
        get() = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
}

// Rx based.
object RxWeatherForecastService : BaseRetrofitBuilder {

    private val service = super.builder
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val weatherApiService: WeatherApiService by lazy {
        service.create(WeatherApiService::class.java)
    }
}

// co-routine - based
object AsyndWeatherForecastService : BaseRetrofitBuilder {

    private val service: Retrofit = builder.build()

    val weatherApiService by lazy {
        service.create(WeatherApiService::class.java)
    }
}
