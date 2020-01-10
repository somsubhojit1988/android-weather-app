package com.example.weatherapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ForecastDao {
    @Insert
    fun insert(forecast: ForecastEntity)

    @Update
    fun update(forecast: ForecastEntity)

    @Query("select * from forecasts where dt = :dt")
    fun get(dt: Long) :ForecastEntity

    @Query("delete from forecasts")
    fun clear()

    @Query("select * from forecasts where dt > :timeStamp order by dt desc")
    fun getForecasts(timeStamp:Long = 0L) : LiveData<List<ForecastEntity>?>
//    fun getForecasts(timeStamp:Long = 0L) : List<ForecastEntity>?
}