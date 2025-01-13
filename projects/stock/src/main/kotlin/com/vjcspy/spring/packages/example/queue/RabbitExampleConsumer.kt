// (mr.vjcspy@gmail.com) 2025
package com.vjcspy.spring.packages.example.queue

import com.vjcspy.spring.packages.example.cfg.RabbitExampleConfig
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitExampleConsumer {
    // Consumer lắng nghe thông điệp từ queue "myQueue"
    @RabbitListener(queues = [RabbitExampleConfig.EXAMPLE_QUEUE], containerFactory = "listenerContainerFactory")
    fun consumeMessage(message: String) {
        runBlocking {
            println("Received message: $message")
            // Giả lập xử lý thông điệp
            delay(Random.nextInt(1000, 5001).toLong()) // Giả lập công việc xử lý mất 1 giây
            println("Done message: $message")
        }
    }
}
