package com.example.weatherapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeatherDao {
    @Insert
    fun insert(weather: WeatherEntity)

    @Update
    fun update(weather: WeatherEntity)

    @Query("select * from current_weather_data where dt = :dt")
    fun get(dt: Long): WeatherEntity?

    @Query(value = "select * from current_weather_data order by dt desc limit 1")
    fun getToday(): LiveData<WeatherEntity?>

    @Query("select * from current_weather_data where dt < :dt order by dt desc")
    fun getOlder(dt: Long): List<WeatherEntity>?

    @Query(value = "delete from current_weather_data")
    fun clear()

    @Query("delete from current_weather_data where dt < :dt")
    fun cleanOlder(dt: Long)
}

