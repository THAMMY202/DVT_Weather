package com.dvt.weather.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(version = 1, entities = [Forecast::class, CurrentWeather::class , FavoriteModel::class], exportSchema = false)
abstract class ForecastDatabase : RoomDatabase() {

    abstract fun forestDao(): ForecastDao

    private class PlayerDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    //val foresDao = database.forestDao()
                    //prePopulateDatabase(foresDao )//forecast
                    //addCurrentData(foresDao )//current
                   // DeleteAll(foresDao)
                }
            }
        }
        private suspend fun prePopulateDatabase(forecastDao: ForecastDao) {

            val forecast = Forecast(0,"2020-08-10",30.0,"htulani",1,90)
            forecastDao.insertAllForecast(forecast)
            Log.d("works", "prePopulateDatabase: ")
        }

        private suspend fun addCurrentData(forecastDao: ForecastDao) {

           // val currentWeather = CurrentWeather(0,12.2,45.0,67.0,"cold","cold","hth")
           // forecastDao.insertCurrentWeather(currentWeather)
           // Log.d("works", "prePopulateDatabase: ")
        }




    }

    companion object {

        @Volatile
        private var INSTANCE: ForecastDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): ForecastDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    ForecastDatabase::class.java,
                    "forecast_database")
                    .addCallback(PlayerDatabaseCallback(coroutineScope))
                     .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}