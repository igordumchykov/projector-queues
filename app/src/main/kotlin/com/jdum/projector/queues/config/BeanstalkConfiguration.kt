package com.jdum.projector.queues.config

import com.dinstone.beanstalkc.BeanstalkClientFactory
import com.dinstone.beanstalkc.JobProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanstalkConfiguration(
        @Value("\${application.beanstalkd.host}")
        val host: String,
        @Value("\${application.beanstalkd.port}")
        val port: Int,
        @Value("\${application.beanstalkd.connection-timeout}")
        val connectionTimeout: Int,
        @Value("\${application.beanstalkd.read-timeout}")
        val readTimeout: Int,
        @Value("\${application.beanstalkd.topic}")
        val topic: String,
) {

    @Bean
    fun beanstalkdConnectionFactory(): BeanstalkClientFactory {
        val config = com.dinstone.beanstalkc.Configuration()
        config.serviceHost = host
        config.servicePort = port
        config.connectTimeout = connectionTimeout
        config.readTimeout = readTimeout
        return BeanstalkClientFactory(config)
    }

    @Bean
    fun beanstalkdProducer(beanstalkdConnectionFactory: BeanstalkClientFactory): JobProducer? {
        return beanstalkdConnectionFactory.createJobProducer(topic)
    }
}