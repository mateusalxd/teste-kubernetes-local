package com.github.mateusalxd.kafkaproducer

import org.apache.camel.builder.RouteBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KafkaProducer(
    @Value("\${app.kafka.topic}") private val topic: String,
    @Value("\${app.timer.period}") private val period: Long,
    @Value("\${app.timer.repeat-count}") private val repeatCount: Long,
) : RouteBuilder() {

    override fun configure() {
        println("Topic: $topic")

        from("timer:newMessage?period=$period&repeatCount=$repeatCount")
            .routeId("new-message")
            .process {
                val timestamp = System.currentTimeMillis() / 1000L
                it.message.body = "Message $timestamp"
                it.message.headers["timestamp"] = timestamp
            }
            .to("kafka:$topic")
    }

}