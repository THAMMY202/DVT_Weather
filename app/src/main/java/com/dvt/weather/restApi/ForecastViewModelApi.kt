package com.dvt.weather.restApi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.dvt.weather.restApi.ForecastWeather.ForecastModel


class ForecastViewModelApi : ViewModel(){
    private val _forecast: MutableLiveData<String> = MutableLiveData()
    val forecast: LiveData<ForecastModel> = Transformations
        .switchMap(_forecast){
            ForecastWeatherRepository.getUser(it)
              }
    fun setCurrentCity(place: String){
        val update = place
        if(_forecast.value == update){
            return
        }
        _forecast.value = update
    }
    fun cancelJobs (){
        ForecastWeatherRepository.cancelJobs()
    }

}