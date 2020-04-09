package com.sd.laborator.microservices

import com.sd.laborator.components.RabbitMQComponent
import com.sd.laborator.interfaces.LibraryDAO
import com.sd.laborator.interfaces.LibraryPrinter
import com.sd.laborator.model.Book
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.sql.Timestamp
import java.util.*
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock


@Controller
class LibraryPrinterMicroservice {
    @Autowired
    private lateinit var amqpTemplate: AmqpTemplate

    @Autowired
    private lateinit var rabbitMQComponent: RabbitMQComponent

    @Autowired
    private lateinit var libraryDAO: LibraryDAO

    @Autowired
    private lateinit var libraryPrinter: LibraryPrinter


    enum class QueryType(val type: String) { AUTHOR("author"), TITLE("title"), PUBLISHER("publisher") }

    @RequestMapping("/print", method = [RequestMethod.GET])
    @ResponseBody
    fun customPrint(@RequestParam(required = true, name = "format", defaultValue = "") format: String): String {
        return when(format) {
            "html" -> libraryPrinter.printHTML(libraryDAO.getBooks())
            "json" -> libraryPrinter.printJSON(libraryDAO.getBooks())
            "raw" -> libraryPrinter.printRaw(libraryDAO.getBooks())
            else -> "Not implemented"
        }
    }

    // THIS IS THE MOST HORRIBLE THING I HAVE EVER DONE!
    private val lock = ReentrantLock()
    private val messageArrived: Condition = lock.newCondition()

    private fun getNewResults(type: QueryType, value: String, format: String): String {
        val books = when (type) {
            QueryType.AUTHOR -> libraryDAO.findAllByAuthor(value)
            QueryType.TITLE -> libraryDAO.findAllByTitle(value)
            QueryType.PUBLISHER -> libraryDAO.findAllByPublisher(value)
        }

        return when (format) {
            "html" -> libraryPrinter.printHTML(books)
            "json" -> libraryPrinter.printJSON(books)
            "raw" -> libraryPrinter.printRaw(books)
            else -> "Not implemented"
        }
    }

    private fun getCacheEntry(type: QueryType, value: String, format: String): String {
        sendMessage("request&${type.type}=$value,format=$format")

        messageArrived.await()

        if (response == "false") {
            println("Inserting in cache")

            val res = getNewResults(type, value, format)

            sendMessage("insert&${type.type}=$value,format=$format&$res")

            return res
        } else {
            val now = Date().time

            if (now - timestamp.time > 3600000) {
                println("Updating old entry in cache")
                val res = getNewResults(type, value, format)

                sendMessage("update&$id&$res")

                return res;
            }
            println("Returning entry from cache")

            return result
        }
    }

    @RequestMapping("/find", method = [RequestMethod.GET])
    @ResponseBody
    fun customFind(@RequestParam(required = false, name = "author", defaultValue = "") author: String,
                   @RequestParam(required = false, name = "title", defaultValue = "") title: String,
                   @RequestParam(required = false, name = "publisher", defaultValue = "") publisher: String,
                   @RequestParam(required = false, name= "format", defaultValue = "json") format: String): String {
        lock.lock()

        val books = when {
            author != "" -> getCacheEntry(QueryType.AUTHOR, author, format)
            title != "" -> getCacheEntry(QueryType.TITLE, title, format)
            publisher != "" -> getCacheEntry(QueryType.PUBLISHER, publisher, format)
            else -> return "Not a valid field"
        }

        lock.unlock()

        return books
    }

    private fun sendMessage(msg: String) {
        this.amqpTemplate.convertAndSend(
            rabbitMQComponent.getExchange(),
            rabbitMQComponent.getRoutingKey(),
            msg)
    }

    private var response: String = "";
    private var timestamp: Timestamp = Timestamp(0)
    private var id: String = ""
    private var result: String = ""

    @RabbitListener(queues = ["\${caching.rabbitmq.queue}"])
    fun receiveMessage(message: String) {
        val parts = message.split('&', limit=4)
        response = parts[0]
        when (response) {
            "true" -> {
                timestamp = Timestamp.valueOf(parts[1])
                id = parts[2]
                result = parts[3]
            }

            "false" -> {
            }
        }

        lock.lock()
        messageArrived.signal()
        lock.unlock()
    }
}