package com.vjcspy.spring.packages.example.queue

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.spring.packages.example.cfg.RabbitExampleConfig
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitExamplePublisher(
    private val rabbitTemplate: RabbitTemplate,
) {
    private val logger = KtLogging.logger {}

    fun sendMessage(message: String) {
        rabbitTemplate.convertAndSend(
            RabbitExampleConfig.EXAMPLE_EXCHANGE,
            RabbitExampleConfig.EXAMPLE_ROUTING_KEY,
            message,
        )
        logger.info("Sent message: $message")
    }
}
