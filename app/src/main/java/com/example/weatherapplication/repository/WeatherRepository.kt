package com.example.weatherapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.weatherapplication.BuildConfig
import com.example.weatherapplication.database.WeatherDb
import com.example.weatherapplication.database.asDomainModel
import com.example.weatherapplication.network.WeatherForecastService
import com.example.weatherapplication.network.asForecastEntity
import com.example.weatherapplication.network.asWeatherEntity
import com.example.weatherapplication.overview.CurrentWeather
import com.example.weatherapplication.overview.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(weatherDb: WeatherDb) {

    private val weatherDao = weatherDb.weatherDbDao

    private val forecastDao = weatherDb.forecastDao

    val currentWeather: LiveData<CurrentWeather> = Transformations.map(weatherDao.getToday()) {
        it?.asDomainModel()
    }

    val forecasts: LiveData<List<Forecast>> = Transformations.map(forecastDao.getForecasts()) {
        it?.asDomainModel()
    }

    suspend fun refreshWeatherReport(latitude: Double = 34.002470, longitude: Double = -84.180720) {
        withContext(Dispatchers.IO) {
            WeatherForecastService.srvc.getWeatherForecast(
                BuildConfig.DARKSKY_APPID,
                latitude,
                longitude
            ).await().apply {
                asWeatherEntity().let {
                    weatherDao.insert(it)
                    weatherDao.cleanOlder(it.dt)
                }
            }.apply {
                asForecastEntity().let {
                    forecastDao.insert(it)
                }
            }
        }
    }
}