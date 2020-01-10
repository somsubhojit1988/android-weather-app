package com.example.weatherapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WeatherDao {
    @Insert
    fun insert(weather: Weather)

    @Update
    fun update(weather: Weather)

    @Query("select * from current_weather_data where fetchDt = :dt")
    fun get(dt: Long): Weather?

    @Query(value = "select * from current_weather_data order by fetchDt desc limit 1")
    fun getToday(): LiveData<Weather?>

    @Query("select * from current_weather_data where fetchDt < :dt order by fetchDt desc")
    fun getOlder(dt: Long): List<Weather>?

    @Query(value = "delete from current_weather_data")
    fun clear()

    // clean all but today
    @Query("delete from current_weather_data where fetchDt < :dt")
    fun cleanOlder(dt: Long)
}

