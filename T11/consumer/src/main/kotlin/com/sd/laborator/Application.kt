package com.sd.laborator

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.sd.laborator")
                .mainClass(Application.javaClass)
                .start()
    }
}