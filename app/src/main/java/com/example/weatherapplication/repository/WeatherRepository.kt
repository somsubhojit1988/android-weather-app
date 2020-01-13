package com.example.weatherapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.weatherapplication.BuildConfig
import com.example.weatherapplication.database.WeatherDb
import com.example.weatherapplication.database.asDomainModel
import com.example.weatherapplication.model.CurrentWeather
import com.example.weatherapplication.model.Forecast
import com.example.weatherapplication.network.WeatherForecastService
import com.example.weatherapplication.network.asForecastEntity
import com.example.weatherapplication.network.asWeatherEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository private constructor(weatherDb: WeatherDb) {

    private val weatherDao = weatherDb.weatherDbDao

    private val forecastDao = weatherDb.forecastDao

    val currentWeather: LiveData<CurrentWeather> = Transformations.map(weatherDao.getToday()) {
        it?.asDomainModel()
    }

    val forecasts: LiveData<List<Forecast>> = Transformations.map(forecastDao.getForecasts()) {
        it?.asDomainModel()
    }

    suspend fun refreshWeatherReport(latitude: Double, longitude: Double) {
        withContext(Dispatchers.IO) {
            WeatherForecastService.srvc.getWeatherForecastAsync(
                BuildConfig.DARKSKY_APPID,
                latitude,
                longitude
            ).apply {
                asWeatherEntity().let {
                    weatherDao.clear()
                    weatherDao.insert(it)
                }
            }.apply {
                asForecastEntity().let {
                    forecastDao.clear()
                    forecastDao.insert(it)
                }
            }
        }
    }

    fun getForecastOf(dt: Long): LiveData<Forecast> = Transformations.map(forecastDao.getLive(dt)) {
        it.asDomainModel()
    }

    companion object {
        private var mInstance: WeatherRepository? = null

        fun getInstance(dB: WeatherDb): WeatherRepository {
            synchronized(this) {
                if (mInstance == null) {
                    mInstance = WeatherRepository(dB)
                }
                return mInstance as WeatherRepository
            }
        }

    }
}