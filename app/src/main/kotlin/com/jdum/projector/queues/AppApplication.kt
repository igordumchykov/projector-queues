package com.jdum.projector.queues

import com.dinstone.beanstalkc.JobProducer
import com.jdum.projector.queues.messaging.RedisMessagePublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration
import java.time.Instant

@SpringBootApplication
class AppApplication(
        @Autowired
        val redisTemplateRdb: RedisTemplate<String, String>,
        @Autowired
        val publisher: RedisMessagePublisher,
        @Autowired
        val beanstalkdProducer: JobProducer,
        @Value("\${application.message-count}")
        val messageCount: Int
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val start = Instant.now()
        println("Start sending and receiving messages: $start")
        testRedisQueues()
//        testBeanstalkdQueue()
        println("Publish $messageCount messages time elapsed: ${Duration.between(start, Instant.now())}")
    }

    fun testRedisQueues() {
        (1..messageCount).toList().forEach { i ->
            publisher.publish("Message $i")
        }
        publisher.publish("KILL")
    }

    fun testBeanstalkdQueue() {
        (1..messageCount).toList().forEach { i ->
            beanstalkdProducer.putJob(1,1,5000, "Message $i".toByteArray())
        }
        beanstalkdProducer.putJob(1,1,5000, "KILL".toByteArray())
        beanstalkdProducer.close()
    }
}

fun main(args: Array<String>) {
    runApplication<AppApplication>(*args)
}
