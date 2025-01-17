package com.vjcspy.spring.tedbed.queue

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
open class RabbitTedBedConfig {
    companion object {
        const val EXAMPLE_ROUTING_KEY = "tedbed.routing.key"
        const val EXAMPLE_QUEUE = "tedbed.queue"
        const val EXAMPLE_EXCHANGE = "tedbed.exchange"
    }

    @Bean("tedBedQueue") // đặt tên rõ ràng
    open fun tedBedQueue(): Queue = Queue(EXAMPLE_QUEUE, true, false, false)

    @Bean("tedBedExchange")
    open fun tedBedExchange(): TopicExchange = TopicExchange(EXAMPLE_EXCHANGE, true, false)

    @Bean
    open fun tedBedQueueBinding(
        @Qualifier("tedBedQueue") exampleQueue: Queue, // thêm Qualifier nếu muốn
        @Qualifier("tedBedExchange") exampleExchange: TopicExchange,
    ): Binding = BindingBuilder.bind(exampleQueue).to(exampleExchange).with(EXAMPLE_ROUTING_KEY)

    @Bean("tedBedListenerContainerFactory")
    open fun tedBedListenerContainerFactory(
        connectionFactory: org.springframework.amqp.rabbit.connection.ConnectionFactory,
    ): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrentConsumers(5) // Số lượng consumer tối thiểu
        factory.setPrefetchCount(1) // Mỗi consumer chỉ nhận 1 message tại một thời điểm
        return factory
    }
}
