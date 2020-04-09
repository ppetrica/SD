package com.sd.tema

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class CachingApp

fun main(args: Array<String>) {
    runApplication<CachingApp>(*args)
}

