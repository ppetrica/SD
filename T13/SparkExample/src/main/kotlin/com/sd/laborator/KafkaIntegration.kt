package com.sd.laborator

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.streaming.Durations
import org.apache.spark.streaming.api.java.JavaInputDStream
import org.apache.spark.streaming.api.java.JavaStreamingContext
import org.apache.spark.streaming.kafka010.*
import scala.Tuple2

fun main() {
    val kafkaParams = mutableMapOf<String, Any>(
        "bootstrap.servers" to "localhost:9092",
        "key.deserializer" to StringDeserializer::class.java,
        "value.deserializer" to StringDeserializer::class.java,
        "group.id" to "use_a_separate_group_id_for_each_stream",
        "auto.offset.reset" to "latest",
        "enable.auto.commit" to false
    )

    val sparkConf = SparkConf().setMaster("local[4]").setAppName("KafkaIntegration")

    val streamingContext = JavaStreamingContext(sparkConf, Durations.seconds(1))

    val topics = listOf("tema_13")

    val stream: JavaInputDStream<ConsumerRecord<String, String>> = KafkaUtils.createDirectStream(
        streamingContext,
        LocationStrategies.PreferConsistent(),
        ConsumerStrategies.Subscribe(topics, kafkaParams)
    )
    stream.mapToPair{record: ConsumerRecord<String, String> -> Tuple2(record.key(), record.value()) }


    val offsetRanges =
        arrayOf( /* topicul, partitia, offset-ul de inceput, offset-ul final */
            OffsetRange.create("tema_13", 0, 0, 100)
        )
    val rdd: JavaRDD<ConsumerRecord<String, String>> = KafkaUtils.createRDD(
        streamingContext.sparkContext(),
        kafkaParams,
        offsetRanges,
        LocationStrategies.PreferConsistent()
    )

    val accumulator = mutableListOf<Pair<Int, Int>>()

    stream.foreachRDD { rdd ->
        rdd.foreachPartition { consumerRecords ->
            consumerRecords.forEach {
                val parts = it.value().split(" ")

                accumulator.add(Pair(parts[0].toInt(), parts[1].toInt()))

                val xs = accumulator.map{ pair -> pair.first }
                val ys = accumulator.map{ pair -> pair.second }

                val medX = xs.sum().toDouble() / accumulator.size
                val medY = ys.sum().toDouble() / accumulator.size

                val top = accumulator.map { pair -> (pair.first - medX) * (pair.second - medY) }.sum()
                val bottom = xs.map{x -> (x - medX)}.map{ dif -> dif * dif }.sum()

                if (bottom > 0) {
                    val beta = top / bottom
                    val alpha = ys[0] - beta * xs[0]

                    println("y = $alpha + $beta * x")
                }
            }

        }
    }

    // stocarea offset-urilor
    stream.foreachRDD { rdd ->
      val offsetRanges = (rdd.rdd() as HasOffsetRanges).offsetRanges()
      (stream.inputDStream() as CanCommitOffsets).commitAsync(offsetRanges)
    }

    streamingContext.start()
    streamingContext.awaitTermination()
}