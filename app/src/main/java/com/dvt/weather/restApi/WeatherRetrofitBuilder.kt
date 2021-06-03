package com.dvt.weather.restApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherRetrofitBuilder {

    const val baseURL = "https://open-api.xyz/"
    const val baseURL2 = "https://api.openweathermap.org/"

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
                .baseUrl(baseURL2)
                .addConverterFactory(GsonConverterFactory.create())

    }
    val apiService:ApiService by lazy {
        retrofitBuilder.build().create(ApiService::class.java)
    }


}