package com.example.firstapp.repository

import com.example.firstapp.server.ApiServices

class CityRepository(val api: ApiServices) {
    fun getCities(q:String, limit:Int) =
        api.getCityList(q, limit, "2f72ae3ec5bf0174496029ab730b1dd0")
}