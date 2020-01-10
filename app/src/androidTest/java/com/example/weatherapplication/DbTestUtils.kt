package com.example.weatherapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.weatherapplication.database.ForecastEntity
import com.example.weatherapplication.database.Weather
import com.example.weatherapplication.network.Current
import com.example.weatherapplication.network.WeatherResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun currentWeather() = Current(
    1578499949,
    "clear",
    "clear-day",
    59.21,
    59.21,
    0.26,
    6.61
)

fun testWeather() =
    Weather(
        System.currentTimeMillis(),
        34.00247,
        -84.18072,
        currentWeather()
    )

fun getWeatherResponse(context: Context) =
    parseWeatherResponse(readJsonFileToString(context, R.raw.weather_forecast))

fun readJsonFileToString(context: Context, resId: Int): String =
    context.resources.openRawResource(resId).bufferedReader().use {
        it.readText()
    }

fun parseWeatherResponse(jsonString: String): WeatherResponse? =
    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter(WeatherResponse::class.java).fromJson(jsonString)

fun weatherResponseToForecastEntityList(res: WeatherResponse) =
    res.daily.data.map {
        ForecastEntity(
            it.time,
            it.summary,
            it.icon,
            it.sunriseTime,
            it.sunsetTime,
            it.temperatureHigh,
            it.temperatureLow,
            it.temperatureMin,
            it.temperatureMax,
            it.cloudCover
        )
    }

@Throws(InterruptedException::class)
fun <T> LiveData<T>.getValueBlocking(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)
    val innerObserver = Observer<T> {
        value = it
        latch.countDown()
    }
    observeForever(innerObserver)
    latch.await(2, TimeUnit.SECONDS)
    return value
}

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
    val observer = Observer<T> { }
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}