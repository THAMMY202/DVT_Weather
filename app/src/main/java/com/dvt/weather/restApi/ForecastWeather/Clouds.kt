package com.dvt.weather.restApi.ForecastWeather
import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int
)