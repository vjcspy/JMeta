package com.vjcspy.spring.tedbed.queue

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
class RabbitTedBedConfig {
    companion object {
        const val EXAMPLE_ROUTING_KEY = "tedbed.routing.key"
        const val EXAMPLE_QUEUE = "tedbed.queue"
        const val EXAMPLE_EXCHANGE = "tedbed.exchange"

        // Thêm constants cho DLX
        const val DLX_EXCHANGE = "tedbed.dlx"
        const val DLQ_QUEUE = "tedbed.dlq"
        const val DLX_ROUTING_KEY = "tedbed.dlx.key"
    }

    @Bean("tedBedDLX")
    fun tedBedDLX(): DirectExchange = DirectExchange(DLX_EXCHANGE, true, false)

    @Bean("tedBedDLQ")
    fun tedBedDLQ(): Queue = Queue(DLQ_QUEUE, true, false, false)

    @Bean
    fun tedBedDLXBinding(
        @Qualifier("tedBedDLQ") dlq: Queue,
        @Qualifier("tedBedDLX") dlx: DirectExchange,
    ): Binding = BindingBuilder.bind(dlq).to(dlx).with(DLX_ROUTING_KEY)

    @Bean("tedBedQueue")
    fun tedBedQueue(): Queue =
        QueueBuilder
            .durable(EXAMPLE_QUEUE)
            .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
            .withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
            // Có thể thêm TTL nếu cần
            // .withArgument("x-message-ttl", 30000) // 30 seconds
            .build()

    @Bean("tedBedExchange")
    fun tedBedExchange(): TopicExchange = TopicExchange(EXAMPLE_EXCHANGE, true, false)

    @Bean
    fun tedBedQueueBinding(
        @Qualifier("tedBedQueue") exampleQueue: Queue,
        @Qualifier("tedBedExchange") exampleExchange: TopicExchange,
    ): Binding = BindingBuilder.bind(exampleQueue).to(exampleExchange).with(EXAMPLE_ROUTING_KEY)

    @Bean("tedBedListenerContainerFactory")
    fun tedBedListenerContainerFactory(
        connectionFactory: org.springframework.amqp.rabbit.connection.ConnectionFactory,
    ): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrentConsumers(1)
        factory.setPrefetchCount(1)

        // https://docs.spring.io/spring-amqp/docs/3.0.0/reference/html/#async-returns
        factory.setDefaultRequeueRejected(false)

        // Khi sử dụng Mono làm kiểu trả về, việc xử lý message là bất đồng bộ
        // Nếu để AcknowledgeMode là AUTO, container sẽ tự động xác nhận message ngay khi method được gọi, không đợi Mono hoàn thành
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL)
        return factory
    }
}
