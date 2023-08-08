package com.jdum.projector.queues.messaging

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.Topic
import org.springframework.stereotype.Component

@Component
class RedisMessagePublisher(
        @Autowired
        val template: RedisTemplate<String, String>,
        @Autowired
        val topic: Topic
):MessagePublisher {
    override fun publish(message: String) {
        template.convertAndSend(topic.topic, message)
    }
}