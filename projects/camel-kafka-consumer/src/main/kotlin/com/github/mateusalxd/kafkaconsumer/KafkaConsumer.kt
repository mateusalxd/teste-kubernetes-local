package com.github.mateusalxd.kafkaconsumer

import org.apache.camel.builder.RouteBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(
    @Value("\${app.kafka.topic}") private val topic: String,
    @Value("\${app.kafka.args}") private val args: String = "",
) : RouteBuilder() {

    override fun configure() {
        val extraArgs = if (args.isEmpty()) "" else "?$args"
        println("Topic: $topic")
        println("Args: $args")

        from("kafka:$topic$extraArgs")
            .routeId("kafka-topic-$topic")
            .log("Message: \${body}")
    }

}