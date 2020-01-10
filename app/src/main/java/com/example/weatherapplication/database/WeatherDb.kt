package com.example.weatherapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Weather::class, ForecastEntity::class],version = 1, exportSchema = false)
abstract class WeatherDb : RoomDatabase() {

    abstract val weatherDbDao: WeatherDao

    abstract val forecastDao: ForecastDao

    companion object {

        @Volatile
        private var sInstance: WeatherDb? = null

        fun getInstance(cntxt: Context): WeatherDb {
            synchronized(this) {
                var tmp = sInstance
                if (tmp == null) {
                    tmp = Room.databaseBuilder(
                        cntxt.applicationContext,
                        WeatherDb::class.java,
                        "weather_db"
                    ).fallbackToDestructiveMigration().build()
                    sInstance = tmp
                }
                return tmp
            }
        }

    }
}