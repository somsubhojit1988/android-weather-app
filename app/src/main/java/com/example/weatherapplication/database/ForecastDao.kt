package com.example.weatherapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecast: ForecastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(forecast: List<ForecastEntity>)

    @Update
    fun update(forecast: ForecastEntity)

    @Query("select * from forecasts where dt = :dt")
    fun get(dt: Long) :ForecastEntity

    @Query("select * from forecasts where dt = :dt")
    fun getLive(dt:Long) :LiveData<ForecastEntity>

    @Query("delete from forecasts")
    fun clear()

    @Query("select * from forecasts where dt > :timeStamp order by dt")
    fun getForecasts(timeStamp:Long = 0L) : LiveData<List<ForecastEntity>>
}