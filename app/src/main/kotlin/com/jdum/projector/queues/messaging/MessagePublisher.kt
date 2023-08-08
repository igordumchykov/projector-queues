package com.jdum.projector.queues.messaging

interface MessagePublisher {
    fun publish(message: String)
}