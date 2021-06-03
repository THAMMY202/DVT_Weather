package com.dvt.weather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "forecast_table",indices = [Index(value = ["timestamp"], unique = true)])
data class Forecast(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "forecastDate") val forecastDate: String = "",
    @ColumnInfo(name = "temperature") val temperature: Double,
    @ColumnInfo(name = "temperatureDescription")val temperatureDescription: String,
    @ColumnInfo(name = "weatherIcon") val weatherIcon:Int,
    @ColumnInfo(name = "timestamp")val timestamp: Int,
)