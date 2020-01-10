package com.example.weatherapplication.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.network.asWeatherEntity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class WeatherDaoTests {

    companion object {
        private const val ONE_SEC = 1000L
        const val ONE_DAY = 24 * 60 * 60 * ONE_SEC
    }

    private lateinit var weatherDao: WeatherDao
    private lateinit var dB: WeatherDb
    private lateinit var applicationContext: Context

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        applicationContext = ApplicationProvider.getApplicationContext<Context>()
        dB = Room.inMemoryDatabaseBuilder(applicationContext, WeatherDb::class.java).build()
        weatherDao = dB.weatherDbDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dB.close()
    }

    @Test
    @Throws(IOException::class)
    fun insert_test() {
        val testEntity: WeatherEntity =
            requireNotNull(getWeatherResponse(applicationContext)?.asWeatherEntity())

        weatherDao.insert(testEntity)
        val retrieved = weatherDao.get(testEntity.dt)
        assertNotNull(retrieved)
        assertThat(retrieved, `is`(testEntity))
    }

    private fun createTestData(): WeatherEntity = getWeatherResponse(
        applicationContext
    )?.asWeatherEntity()
        ?: throw IllegalArgumentException("could not fetch local weather entity")

    private fun newTestDataFrom(testEntity: WeatherEntity): WeatherEntity = WeatherEntity(
        dt = testEntity.dt - ONE_DAY,
        latitude = testEntity.latitude,
        longitude = testEntity.longitude,
        summary = testEntity.summary,
        icon = testEntity.icon,
        temperature = testEntity.temperature,
        maxTemperature = testEntity.maxTemperature,
        minTemperature = testEntity.minTemperature,
        apparentTemperature = testEntity.minTemperature,
        humidity = testEntity.humidity,
        windSpeed = testEntity.windSpeed
    )

    private fun modifyTestData(testEntity: WeatherEntity): WeatherEntity = WeatherEntity(
        dt = testEntity.dt,
        latitude = testEntity.latitude,
        longitude = testEntity.longitude,
        summary = testEntity.summary,
        icon = testEntity.icon,
        temperature = testEntity.temperature,
        maxTemperature = testEntity.maxTemperature - 100,
        minTemperature = testEntity.minTemperature + 100,
        apparentTemperature = testEntity.minTemperature,
        humidity = testEntity.humidity,
        windSpeed = testEntity.windSpeed
    )

    @Test
    @Throws(IOException::class)
    fun getToday_test() {
        val testEntity = createTestData()
        weatherDao.insert(testEntity)
        val preDatedEntity = newTestDataFrom(testEntity)
        weatherDao.insert(preDatedEntity)
        val result: WeatherEntity? = weatherDao.getToday().getOrAwaitValue()
        assertNotNull(result)
        assertThat(result, `is`(testEntity))
    }

    @Test
    @Throws(IOException::class)
    fun update_rest() {
        val testEntity = createTestData()
        val updatedEntity = modifyTestData(testEntity)

        weatherDao.insert(testEntity)
        weatherDao.update(updatedEntity)

        val res = weatherDao.get(testEntity.dt)
        assertNotNull(res)
        assertThat(res!!, `is`(updatedEntity))
        assertThat(res, `is`(updatedEntity))
    }

    @Test
    @Throws(IOException::class)
    fun getOlder_test() {
        val testEntity = createTestData()
        val predated1 = newTestDataFrom(testEntity)
        val predated2 = newTestDataFrom(predated1)

        weatherDao.insert(testEntity)
        weatherDao.insert(predated1)
        weatherDao.insert(predated2)

        val res = weatherDao.getOlder(testEntity.dt)
        assertNotNull(res)
        assertEquals(res.size, 2)
        assertEquals(res[0], predated1)
        assertEquals(res[1], predated2)
    }

    @Test
    @Throws(IOException::class)
    fun clear_test() {
        createTestData().also {
            weatherDao.insert(it)
            weatherDao.insert(newTestDataFrom(it))
        }

        assertNotNull(weatherDao.getToday().getOrAwaitValue())
        var older = weatherDao.getOlder(System.currentTimeMillis())
        assertEquals(older.size, 2)

        weatherDao.clear()
        older = weatherDao.getOlder(System.currentTimeMillis())
        assert(older.isEmpty())
    }

    @Test
    @Throws(IOException::class)
    fun cleanOlderTest() {
        val testData = createTestData()
        val preDated = newTestDataFrom(testData)
        weatherDao.insert(testData)
        weatherDao.insert(preDated)

        weatherDao.cleanOlder(testData.dt)
        val res = weatherDao.getOlder(testData.dt)
        assert(res.isEmpty())
    }
}