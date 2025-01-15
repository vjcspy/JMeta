package com.vjcspy.spring.tedbed.queue

import com.vjcspy.kotlinutilities.log.KtLogging
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class RabbitTedBedPublisher(
    private val rabbitTemplate: RabbitTemplate,
) {
    private val logger = KtLogging.logger {}

    fun sendMessage(message: String) {
        rabbitTemplate.convertAndSend(
            RabbitTedBedConfig.EXAMPLE_EXCHANGE,
            RabbitTedBedConfig.EXAMPLE_ROUTING_KEY,
            message,
        )
        logger.info("Sent message: $message")
    }
}
