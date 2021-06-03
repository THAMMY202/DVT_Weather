package com.dvt.weather.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ForecastDao {
    //1 Forecast
    @Delete
    suspend fun deleteForecast(forecast: Forecast)

    @Update
    suspend fun updateForecast(forecast: Forecast)

    @Query("SELECT * FROM forecast_table WHERE id = :id")
    fun getForecast(id: Int): LiveData<Forecast>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllForecast(forest: Forecast)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favoriteModel: FavoriteModel)

    @Query("SELECT * FROM forecast_table where id in (SELECT id FROM forecast_table ORDER BY id DESC LIMIT 5)")
    fun getAllForecast(): LiveData<List<Forecast>>

    @Query("SELECT * FROM favorite_table")
    fun getAllFavorite(): LiveData<List<FavoriteModel>>

    //2 CurrentWeather
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCurrentWeather(current: CurrentWeather)

    @Query("SELECT * FROM current_table order by id desc limit 1")
    fun getCurrent(): LiveData<List<CurrentWeather>>

    @Query(" DELETE FROM forecast_table")
    fun deleteAll()
    @Query(" DELETE FROM current_table")
    fun deleteAllCurrent()

}