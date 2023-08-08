package com.jdum.projector.queues.config

import com.jdum.projector.queues.messaging.MessagePublisher
import com.jdum.projector.queues.messaging.RedisMessagePublisher
import com.jdum.projector.queues.messaging.RedisMessageSubscriber
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.Topic
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter


@Configuration
class RedisConfiguration(
        @Value("\${application.redis.host}")
        val host: String,
        @Value("\${application.redis.port}")
        val port: Int,
        @Value("\${application.redis.topic}")
        val topic: String
) {

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory? {
        return JedisConnectionFactory(RedisStandaloneConfiguration(host, port))
    }

    @Bean
    @Primary
    fun redisTemplate(): RedisTemplate<String, String>? {
        val template = StringRedisTemplate()
        template.connectionFactory = jedisConnectionFactory()
        return template
    }

    @Bean
    fun messageListener(): MessageListenerAdapter? {
        return MessageListenerAdapter(RedisMessageSubscriber())
    }

    @Bean
    fun redisContainer(): RedisMessageListenerContainer? {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(jedisConnectionFactory()!!)
        container.addMessageListener(messageListener()!!, topic())
        return container
    }

    @Bean
    fun redisPublisher(): MessagePublisher? {
        return RedisMessagePublisher(redisTemplate()!!, topic())
    }

    @Bean
    fun topic(): Topic {
        return ChannelTopic(topic)
    }
}