package com.sd.tema.interfaces

import com.sd.tema.pojo.WeatherForecastData

interface WeatherForecastInterface {
    fun getForecastData(locationId: Int): WeatherForecastData
}