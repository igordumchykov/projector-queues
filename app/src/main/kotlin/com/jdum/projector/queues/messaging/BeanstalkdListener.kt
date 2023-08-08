package com.jdum.projector.queues.messaging

import com.dinstone.beanstalkc.BeanstalkClientFactory
import com.dinstone.beanstalkc.JobConsumer
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Component
class BeanstalkdListener(
        @Autowired
        val beanstalkdConnectionFactory: BeanstalkClientFactory,
        @Value("\${application.beanstalkd.topic}")
        val topic: String,
) {

    val executor: ExecutorService = Executors.newSingleThreadExecutor()
    var consumer: JobConsumer? = null
    @PostConstruct
    fun init() {
        consumer = beanstalkdConnectionFactory.createJobConsumer(topic)
        executor.execute {
            while (true) {
                val job = consumer?.reserveJob(1)
                if (job?.data?.let { String(it) } == "KILL") {
                    println("Message received: ${String(job.data)}, time: ${Instant.now()}")
                    break
                }
            }
            consumer?.deleteJob(1)
        }
    }

    @PreDestroy
    fun close() {
        consumer?.close()
        executor.shutdownNow()
    }
}