package com.example.weatherapplication.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.network.asForecastEntity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ForecastDaoTest {

    private val applicationContext = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var dB: WeatherDb
    private lateinit var forecastsDao: ForecastDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        dB = WeatherDb.getInstance(applicationContext)
        forecastsDao = dB.forecastDao
    }

    @After
    @Throws(IOException::class)
    fun tearDown() = dB.close()

    private fun createTestData(): List<ForecastEntity> =
        requireNotNull(getWeatherResponse(applicationContext)?.asForecastEntity())

    @Test
    @Throws(IOException::class)
    fun insert_test() {
        val testData = createTestData()
        forecastsDao.clear()
        forecastsDao.insert(testData)
        val res = forecastsDao.getForecasts().getOrAwaitValue()
        assertEquals(res.size, testData.size)
    }

    @Test
    @Throws(IOException::class)
    fun update_test() {
    }
}