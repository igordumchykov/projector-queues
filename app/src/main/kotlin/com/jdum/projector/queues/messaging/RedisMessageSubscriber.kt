package com.jdum.projector.queues.messaging

import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class RedisMessageSubscriber : MessageListener {
    override fun onMessage(message: Message, pattern: ByteArray?) {
        messageList.add(message.toString())
        if (message.toString() == "KILL") {
            println("Message received: $message, time: ${Instant.now()}")
        }
    }

    companion object {
        var messageList: MutableList<String> = ArrayList()
    }
}