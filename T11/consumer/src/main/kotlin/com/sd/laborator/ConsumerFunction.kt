package com.sd.laborator;

import io.micronaut.function.executor.FunctionInitializer
import io.micronaut.function.FunctionBean;
import java.util.function.Function;

@FunctionBean("consumer")
class ConsumerFunction : FunctionInitializer(), Function<Request, Response> {

    override fun apply(msg : Request) : Response {
        val resp = Response()

        val regex = """<title>(.*?)</title>.*?<link href=\"(.*?)\"""".toRegex()
        val matchResult = regex.findAll(msg.site)

        resp.result = matchResult.map{ listOf(it.groupValues[1], it.groupValues[2]) }.toList()

        return resp
    }
}

/**
 * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
 * where the argument to echo is the JSON to be parsed.
 */
fun main(args : Array<String>) { 
    val function = ConsumerFunction()
    function.run(args, { context -> function.apply(context.get(Request::class.java))})
}