package com.github.mateusalxd.kafkaconsumer

import org.apache.camel.builder.RouteBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ExitCodeGenerator
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.exitProcess

// https://stackoverflow.com/questions/60481228/apache-camel-spring-boot-graceful-shutdown-of-the-application-after-processing

@Component
class KafkaConsumer(
    @Value("\${app.kafka.topic}") private val topic: String,
    @Value("\${app.kafka.args}") private val args: String = "",
    @Value("\${app.check_interval_ms}") private val checkIntervalMs: Long,
    @Value("\${app.maximum_idle_time_ms}") private val maximumIdleTimeMs: Long,
    @Autowired private val applicationContext: ApplicationContext,
) : RouteBuilder() {

    private val lastMessage = AtomicLong(System.currentTimeMillis() / 1000L)

    override fun configure() {
        val extraArgs = if (args.isEmpty()) "" else "?$args"
        println("Topic: $topic")
        println("Args: $args")

        from("timer:check?period=$checkIntervalMs")
            .routeId("check")
            .process {
                Thread {
                    try {
                        val interval = (System.currentTimeMillis() / 1000L) - lastMessage.get()
                        if (interval > maximumIdleTimeMs / 1000) {
                            it.context.stop()
                            val returnCode = 0
                            SpringApplication.exit(applicationContext, ExitCodeGenerator { returnCode })
                            exitProcess(returnCode)
                        }
                    } catch (e: Exception) {
                        throw RuntimeException(e)
                    }
                }.start()
            }
            .end()


//        from("timer:newMessage?period=2000&repeatCount=5")
//            .process {
//                println("New message")
//                lastMessage.set(System.currentTimeMillis() / 1000L)
//            }

        from("kafka:$topic${extraArgs}")
            .routeId("kafka-topic-$topic")
            .process {
                println("Message: ${it.message.body}")
                lastMessage.set(System.currentTimeMillis() / 1000L)
            }
    }

}