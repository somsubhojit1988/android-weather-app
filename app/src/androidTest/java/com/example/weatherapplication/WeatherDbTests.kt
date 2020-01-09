package com.example.weatherapplication

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.database.WeatherDao
import com.example.weatherapplication.database.WeatherDb
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WeatherDbTests {

    companion object {
        private const val ONE_SEC = 1000L
        const val ONE_DAY = 24 * 60 * 60 * ONE_SEC
    }

    private lateinit var weatherDao: WeatherDao

    private lateinit var dB: WeatherDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dB = Room.inMemoryDatabaseBuilder(context, WeatherDb::class.java).build()
        weatherDao = dB.weatherDbDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dB.close()
    }

    @Test
    @Throws(IOException::class)
    fun writeWeatherAndReadInTest() {
        val weatherObj = testWeather().apply {
            fetchDt = System.currentTimeMillis()
        }
        weatherDao.insert(weatherObj)
        val byFetchDt = weatherDao.get(weatherObj.fetchDt)
        assert(weatherObj == byFetchDt)
    }

    @Test
    @Throws
    fun getTodayWeatherTest() {
        val timeNow = System.currentTimeMillis()
        val weatherObj = testWeather().apply {
            fetchDt = timeNow
        }
        weatherDao.insert(weatherObj)

        // insert with fetchSate set to yesterdays
        weatherObj.apply {
            fetchDt -= ONE_DAY
        }
        weatherDao.insert(weatherObj)
        val weatherToday = weatherDao.getToday()

        assert(weatherToday.value?.fetchDt == timeNow)
    }

    @Test
    @Throws(IOException::class)
    fun testCleanAllBeforeToday() {
        val fetchTime = System.currentTimeMillis()
        val weatherObj = testWeather().apply {
            fetchDt = fetchTime
        }
        weatherDao.also {
            // insert yesterday's
            it.insert(weatherObj.apply {
                fetchDt -= ONE_DAY
            })
            it.insert(weatherObj.apply {
                fetchDt -= 2 * ONE_DAY
            })
            it.insert(weatherObj.apply {
                fetchDt -= 3 * ONE_DAY
            })

            assert(weatherDao.get(fetchTime - ONE_DAY) != null)

            assert(weatherDao.get(fetchTime - 2 * ONE_DAY) != null)

            assert(weatherDao.get(fetchTime - 3 * ONE_DAY) != null)

            it.cleanOlder(fetchTime)
        }

        assert(weatherDao.get(fetchTime - ONE_DAY) == null)

        assert(weatherDao.get(fetchTime - 2 * ONE_DAY) == null)

        assert(weatherDao.get(fetchTime - 3 * ONE_DAY) == null)
    }


    @Test
    @Throws(IOException::class)
    fun getOlderTest() {
        val weatherObj = testWeather()
        // series of inserts & finally clean
        weatherDao.also {
            // 1. insert
            it.insert(weatherObj)

            //2. insert
            it.insert(weatherObj.apply {
                fetchDt -= ONE_DAY
            })

            //3. insert
            it.insert(weatherObj.apply {
                fetchDt -= 3 * ONE_DAY
            })
        }
        assert(weatherDao.getOlder(weatherObj.fetchDt)?.size == 2)
    }

    @Test
    @Throws(IOException::class)
    fun cleanTest() {
        val weatherObj = testWeather()
        // series of inserts & finally clean
        weatherDao.also {
            // 1. insert
            it.insert(weatherObj)

            //2. insert
            it.insert(weatherObj.apply {
                fetchDt -= ONE_DAY
            })

            //3. insert
            it.insert(weatherObj.apply {
                fetchDt -= 3 * ONE_DAY
            })
            // CLEAN
            it.clear()
        }

        assert(weatherDao.getToday().value == null)
    }

}