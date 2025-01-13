// (mr.vjcspy@gmail.com) 2025
package com.vjcspy.spring.packages.example.cfg

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitExampleConfig {
    companion object {
        const val EXAMPLE_ROUTING_KEY = "example.routing.key"
        const val EXAMPLE_QUEUE = "example.queue"
        const val EXAMPLE_EXCHANGE = "example.exchange"
    }

    // Queue: tạo queue có tên "example-queue"
    @Bean
    fun myQueue(): Queue {
        return Queue(EXAMPLE_QUEUE, true, false, false) // true: queue là durable
    }

    // Exchange: tạo một exchange kiểu Topic
    @Bean
    fun myExchange(): TopicExchange {
        return TopicExchange(EXAMPLE_EXCHANGE, true, false) // durable, non-auto-delete
    }

    // Binding: liên kết giữa queue và exchange với routing key "my.routing.key"
    @Bean
    fun binding(
        myQueue: Queue,
        myExchange: TopicExchange,
    ): Binding = BindingBuilder.bind(myQueue).to(myExchange).with(EXAMPLE_ROUTING_KEY)

    // Cấu hình SimpleRabbitListenerContainerFactory để áp dụng PrefetchCount và concurrency cho listener
    @Bean
    fun listenerContainerFactory(connectionFactory: ConnectionFactory): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrentConsumers(3) // Số lượng consumer tối thiểu
        factory.setPrefetchCount(1) // Mỗi consumer chỉ nhận 1 message tại một thời điểm
        return factory
    }
}
