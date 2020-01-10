package com.example.weatherapplication

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.database.ForecastDao
import com.example.weatherapplication.database.ForecastEntity
import com.example.weatherapplication.database.WeatherDb
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ForeCastDbTests {

    private lateinit var context: Context

    private lateinit var dB: WeatherDb

    private lateinit var forecastDao: ForecastDao

    private lateinit var forecasts: List<ForecastEntity>


    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        println("createDb()")
        context = ApplicationProvider.getApplicationContext()
        dB = Room.inMemoryDatabaseBuilder(context, WeatherDb::class.java).build()
        forecastDao = dB.forecastDao
    }

    @Before
    fun prepareTestData() {
        forecasts = getWeatherResponse(context)?.let {
            weatherResponseToForecastEntityList(it)
        }?.let {
            it.subList(1, it.size)
        } ?: throw IllegalArgumentException("forecast list empty")
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = dB.close()

    @Test
    fun insertAndGetTest() {
        forecasts?.let { forecastList ->
            forecastList.forEach { entity -> forecastDao.insert(entity) }
            forecastList.forEach {
                val res = forecastDao.get(it.dt)
                assert(it == res)
            }
        }
    }

    @Test
    fun updateTest() {
        val forecast = forecasts[1]

        forecastDao.insert(forecast)

        val iconVal = forecast.icon
        forecast.icon = "modified"
        forecastDao.update(forecast)

        val retrieved = forecastDao.get(forecast.dt)
        assert(retrieved.icon != iconVal)
        assert(retrieved.icon == "modified")
    }

    @Test
    fun getAllForecasts() {
        forecasts.forEach {
            forecastDao.insert(it)
        }

        val forecasts = forecastDao.getForecasts().getOrAwaitValue()
        assertThat(forecasts?.isNotEmpty() ?: false, `is`(true))
    }
}