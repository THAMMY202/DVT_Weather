package com.dvt.weather.restApi.ForecastWeather
import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String
)