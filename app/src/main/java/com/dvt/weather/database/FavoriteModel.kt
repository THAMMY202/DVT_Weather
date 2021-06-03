package com.dvt.weather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table",indices = [Index(value = ["place"], unique = true)])
data class FavoriteModel(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id") var id: Int = 0,
        @ColumnInfo(name = "place") val place: String = "",
        @ColumnInfo(name = "temperature") val temperature: Double,

)

