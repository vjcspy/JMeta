package com.vjcspy.spring.tedbed.queue

import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class RabbitTedBedConsumer {
    // Consumer lắng nghe thông điệp từ queue "myQueue"
    @RabbitListener(queues = [RabbitTedBedConfig.EXAMPLE_QUEUE], containerFactory = "tedBedListenerContainerFactory")
    fun consumeMessage(message: String) {
        runBlocking {
            println("Received message: $message")
            // Giả lập xử lý thông điệp
            delay(Random.nextInt(1000, 5001).toLong()) // Giả lập công việc xử lý mất 1 giây
            println("Done message: $message")
        }
    }
}
