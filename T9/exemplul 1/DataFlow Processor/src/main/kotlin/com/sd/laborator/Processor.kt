package com.sd.laborator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Processor
import org.springframework.integration.annotation.Transformer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.DateFormat
import java.text.SimpleDateFormat

@EnableBinding(Processor::class)
@SpringBootApplication
open class SpringDataFlowTimeProcessorApplication {
    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    fun transform(command: String?): Any? {
        println("Executing command $command")
        val proc: Process = Runtime.getRuntime().exec(command)
        println("Processing")

        val input = BufferedReader(InputStreamReader(proc.inputStream))

        var c: Int = input.read()
        println("Read")

        val result = StringBuilder()

        while (c != -1) {
            result.append(c.toChar())
            c = input.read()
            println("Read")
        }

        return result.toString()
    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataFlowTimeProcessorApplication>(*args)
}