package com.example.weatherapplication.overview

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherapplication.database.WeatherDb
import com.example.weatherapplication.model.CurrentWeather
import com.example.weatherapplication.model.Forecast
import com.example.weatherapplication.repository.WeatherRepository

class WeatheroverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
        WeatherRepository.getInstance(WeatherDb.getInstance(application.applicationContext))

    val currentWeather: LiveData<CurrentWeather> = repository.currentWeather

    val forecast: LiveData<List<Forecast>> = repository.forecasts

    private val _errorMessage = MutableLiveData<String>()

    private val _refreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    val refreshing: LiveData<Boolean>
        get() = _refreshing

    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        refreshWeatherDataFromRepo()
    }

    fun onRefresh() {
        refreshWeatherDataFromRepo()
    }

    private fun refreshWeatherDataFromRepo(
        latitude: Double = 34.002470,
        longitude: Double = -84.180720
    ) {
        repository.rxRefreshWeather(latitude, longitude)
    }


    override fun onCleared() {
        super.onCleared()
        repository.stopRefresh()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatheroverviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatheroverviewViewModel(application) as T
            }
            throw IllegalArgumentException("unable to construct viewModel")
        }
    }
}