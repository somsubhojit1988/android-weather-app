package com.example.weatherapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ForecastDao {
    @Insert
    fun insert(forecast: Forecast)

    @Update
    fun update(forecast: Forecast)

    @Query("select * from forecasts where dt = :dt")
    fun get(dt: Long) :Forecast

    @Query("delete from forecasts")
    fun clear()

    @Query("select * from forecasts where dt > :timeStamp")
    fun getForecasts(timeStamp:Long) : LiveData<List<Forecast>>
}