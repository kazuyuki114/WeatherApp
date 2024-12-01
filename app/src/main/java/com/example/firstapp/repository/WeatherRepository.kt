package com.example.firstapp.repository

import com.example.firstapp.server.ApiServices

class WeatherRepository(val api:ApiServices) {
    fun getCurrentWeather(lat:Double, lon:Double, unit:String) =
        api.getCurrentWeather(lat, lon, unit, "2f72ae3ec5bf0174496029ab730b1dd0")
    fun getForecastWeather(lat:Double, lon:Double, unit:String) =
        api.getForecastWeather(lat, lon, unit, "2f72ae3ec5bf0174496029ab730b1dd0")
}