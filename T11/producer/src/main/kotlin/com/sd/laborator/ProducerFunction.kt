package com.sd.laborator;

import io.micronaut.function.executor.FunctionInitializer
import io.micronaut.function.FunctionBean;
import java.net.URL
import java.util.function.Supplier


@FunctionBean("producer")
class ProducerFunction : FunctionInitializer(), Supplier<Producer> {
    override fun get(): Producer {
        val msg = Producer()
        msg.site = URL("https://xkcd.com/atom.xml").readText()
        return msg
    }
}

/**
 * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
 * where the argument to echo is the JSON to be parsed.
 */
fun main(args : Array<String>) { 
    val function = ProducerFunction()
    function.run(args, { context -> function.get()})
}