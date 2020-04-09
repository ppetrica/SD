package com.sd.tema.services


import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*


@Service
class TimeService {
    private val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

    fun getCurrentTime():String {
        return formatter.format(Date())
    }
}
