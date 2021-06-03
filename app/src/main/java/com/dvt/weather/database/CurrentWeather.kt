package com.dvt.weather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "current_table")
data class CurrentWeather (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "temperature_min") val temperature_min: Long,
    @ColumnInfo(name = "temperature_current") val temperature_current: Long,
    @ColumnInfo(name = "temperature_max") val temperature_max: Long,
    @ColumnInfo(name = "temperature") val temperature: String,
    @ColumnInfo(name = "temperatureDescription") val temperatureDescription: String,
    @ColumnInfo(name = "weatherIcon") val weatherIcon:Int,
    @ColumnInfo(name = "feelLike") val feelLike: Long,
    @ColumnInfo(name = "updatedAt") val updatedAt: String,

)