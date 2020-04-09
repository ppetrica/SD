package com.sd.tema.microservices

import com.sd.tema.components.RabbitMQComponent
import com.sd.tema.services.CachingServiceDAO
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

@Service
class CachingMicroservice {
    @Autowired
    private lateinit var rabbitMQComponent: RabbitMQComponent;

    private lateinit var amqpTemplate: AmqpTemplate;

    @Autowired
    private lateinit var cachingDAO: CachingServiceDAO

    @Autowired
    fun initTemplate() {
        amqpTemplate = rabbitMQComponent.rabbitTemplate();
    }

    @RabbitListener(queues = ["\${caching.rabbitmq.queue}"])
    fun receiveMessage(message: String) {
        val parts = message.split('&', limit=2)

        when (parts[0]) {
            "request" -> {
                println("Received request message")
                val query = parts[1]

                val entry = cachingDAO.getEntryByQuery(query)

                if (entry.isEmpty) {
                    sendMessage("false")
                } else {
                    val result = entry.get()

                    sendMessage("true&${result.timestamp}&${result.id}&${result.result}")
                }
            }
            "update" -> {
                println("Received update message")
                val rest = parts[1].split('&', limit=2)
                val id = rest[0]
                val timestamp = Timestamp(Date().time)
                val result = rest[1]

                cachingDAO.updateEntry(id.toInt(), timestamp, result)
            }
            "insert" -> {
                println("Received insert message")
                val rest = parts[1].split('&', limit=2)
                val query = rest[0]
                val timestamp = Timestamp(Date().time)
                val result = rest[1]

                cachingDAO.insertEntry(query, timestamp, result)
            }
        }
    }

    private fun sendMessage(msg: String) {
        this.amqpTemplate.convertAndSend(
            rabbitMQComponent.getExchange(),
            rabbitMQComponent.getRoutingKey(),
            msg)
    }
}