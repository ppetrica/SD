package com.sd.tema.controllers

import com.sd.tema.interfaces.WeatherForecastInterface
import com.sd.tema.pojo.WeatherForecastData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class WeatherAppController {
    @Autowired
    private lateinit var weatherForecastService: WeatherForecastInterface

    @RequestMapping("/getforecast/{location}", method=[RequestMethod.GET])
    @ResponseBody
    fun getForecast(@PathVariable location: String): String {
        val rawForecastData: WeatherForecastData = weatherForecastService.getForecastData(location)

        return rawForecastData.toString()
    }
}
