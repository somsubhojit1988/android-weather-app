package com.example.weatherapplication.overview

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherapplication.database.WeatherDb
import com.example.weatherapplication.repository.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

class WeatheroverviewViewModel(application: Application) : AndroidViewModel(application) {
    private val job = Job()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    private val repository =
        WeatherRepository(WeatherDb.getInstance(application.applicationContext))

    val currentWeather: LiveData<CurrentWeather> = repository.currentWeather

    val forecast: LiveData<List<Forecast>> = repository.forecasts

    private val _errorMessage = MutableLiveData<String>()

    private val _refreshing: MutableLiveData<Boolean> = MutableLiveData(false)

    private var _latitude :Double = 34.002470

    private var _longitude: Double = -84.180720

    val refreshing :LiveData<Boolean>
        get() = _refreshing

    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        refreshWeatherDataFromRepo()
    }

    fun onRefresh() {
        refreshWeatherDataFromRepo()
    }


    private fun refreshWeatherDataFromRepo(latitude: Double = 34.002470, longitude: Double = -84.180720) {
        coroutineScope.launch {
            try {
                _refreshing.value = true
                repository.refreshWeatherReport(latitude, longitude)
                Timber.d("data fetched from repository >>")
            } catch (e: IOException) {
                "error fetching Weather API: ${e.message}".apply {
                    _errorMessage.value = this
                    Timber.e(this)
                }
                e.printStackTrace()
            } finally {
                _refreshing.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
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