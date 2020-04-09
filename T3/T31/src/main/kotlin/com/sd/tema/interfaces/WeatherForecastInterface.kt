package com.sd.tema.interfaces

import com.sd.tema.pojo.WeatherForecastData

interface WeatherForecastInterface {
    fun getForecastData(city: String): WeatherForecastData
    fun getForecastData(locationID: Int): WeatherForecastData
}