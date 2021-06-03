package com.dvt.weather.restApi.ForecastWeather
import com.google.gson.annotations.SerializedName

data class ForecastModel(
    @SerializedName("city")
    val city: City,
    @SerializedName("cnt")
    val cnt: Int,
    @SerializedName("cod")
    val cod: String,
    @SerializedName("list")
    val list: List<ListData>,
    @SerializedName("message")
    val message: Int
)