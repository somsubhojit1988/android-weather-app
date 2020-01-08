package com.example.weatherapplication.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weatherapplication.BuildConfig
import com.example.weatherapplication.formatDate
import com.example.weatherapplication.network.WeatherForecastService
import com.example.weatherapplication.network.WeatherResponse
import com.example.weatherapplication.weatherResponseToForecastList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class WeatherOverviewViewModel : ViewModel() {

    private val job = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    private val _weatherResponse = MutableLiveData<WeatherResponse>()

    val currentWeather: LiveData<CurrentWeather> = Transformations.map(_weatherResponse) {
        CurrentWeather(
            it.currently.time,
            it.currently.temperature,
            it.currently.apparentTemperature,
            it.currently.summary,
            it.daily.data[0].temperatureMax,
            it.daily.data[0].temperatureMin,
            it.currently.icon
        )
    }

    val foreCast: LiveData<List<Forecast>> =
        Transformations.map(_weatherResponse, ::weatherResponseToForecastList)

    private  val _errorMessage = MutableLiveData<String>()

    val errorMessage :LiveData<String>
        get() = _errorMessage

    init {
        getWeatherForecast()
    }

    private fun getWeatherForecast(latitude: Double = 34.002470, longitude: Double = -84.180720) {
        coroutineScope.launch {
            val weatherResponseDeferred = WeatherForecastService.srvc.getWeatherForecast(
                BuildConfig.DARKSKY_APPID,
                latitude,
                longitude
            )
            try {
                _weatherResponse.value = weatherResponseDeferred.await()
            } catch (e: Exception) {
                val errMsg = "error fetching api: ${e.message}"
                _errorMessage.value = errMsg
                Timber.e(errMsg)
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}