package com.example.weatherapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.weatherapplication.BuildConfig
import com.example.weatherapplication.database.ForecastEntity
import com.example.weatherapplication.database.WeatherDb
import com.example.weatherapplication.database.WeatherEntity
import com.example.weatherapplication.database.asDomainModel
import com.example.weatherapplication.model.CurrentWeather
import com.example.weatherapplication.model.Forecast
import com.example.weatherapplication.network.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class WeatherRepository private constructor(weatherDb: WeatherDb) {

    private val weatherDao = weatherDb.weatherDbDao

    private val forecastDao = weatherDb.forecastDao

    private val rxForecastService = RxWeatherForecastService.weatherApiService

    private lateinit var mDisposable: Disposable

    private var job = Job()

    private var repositoryScope = CoroutineScope(Dispatchers.IO + job)

    val currentWeather: LiveData<CurrentWeather> = Transformations.map(weatherDao.getToday()) {
        it?.asDomainModel()
    }

    val forecasts: LiveData<List<Forecast>> = Transformations.map(forecastDao.getForecasts()) {
        it?.asDomainModel()
    }

    private suspend fun persistWeather(w: WeatherEntity) =
        withContext(Dispatchers.IO) {
            weatherDao.clear()
            weatherDao.insert(w)
        }

    private suspend fun persistForeCastList(l: List<ForecastEntity>) =
        withContext(Dispatchers.IO) {
            forecastDao.clear()
            forecastDao.insert(l)
        }

    private val mObserver = Consumer<WeatherResponse> {
        it?.apply {
            repositoryScope.launch {
                persistWeather(asWeatherEntity())
                persistForeCastList(asForecastEntity())
            }
        }
    }

    // Rx-java Rest API call
    fun rxRefreshWeather(latitude: Double, longitude: Double) {
        mDisposable = rxForecastService.getWeatherForecast(
            appId = BuildConfig.DARKSKY_APPID,
            lat = latitude,
            lon = longitude
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(mObserver)
    }

    // need for Rx
    fun stopRefresh() {
        mDisposable.takeIf {::mDisposable.isInitialized }?.let{
            mDisposable.dispose()
        }
        job.cancel()
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