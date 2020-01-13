package com.example.weatherapplication.forecastdetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapplication.database.WeatherDb
import com.example.weatherapplication.model.Forecast
import com.example.weatherapplication.repository.WeatherRepository

class ForecastDetailsViewModel(application: Application, forecastDt: Long) :
    AndroidViewModel(application) {

    private val dB = WeatherDb.getInstance(application.applicationContext)

    private val repository = WeatherRepository.getInstance(dB)

    val forecast: LiveData<Forecast> = repository.getForecastOf(forecastDt)


    class Factory(private val application: Application, private val id: Long) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ForecastDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ForecastDetailsViewModel(application, id) as T
            }
            throw IllegalArgumentException("Cannot create Forecast Details viewModel")
        }
    }

}
