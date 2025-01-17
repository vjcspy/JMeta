package com.vjcspy.spring.tedbed.queue

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.spring.base.context.ApplicationContext
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.fromRunnable
import reactor.core.scheduler.Schedulers

@Component
class RabbitTedBedConsumer {
    private val logger = KtLogging.logger {}

    // Consumer lắng nghe thông điệp từ queue "myQueue"
    @RabbitListener(queues = [RabbitTedBedConfig.EXAMPLE_QUEUE], containerFactory = "tedBedListenerContainerFactory")
    fun consumeMessage(message: String): Mono<Void> =
        Mono.defer {
            Mono
                .just(message)
                .flatMap { msg ->
                    Mono.deferContextual { ctx ->
                        val correlationId = ctx.get<String>(ApplicationContext.CORRELATION_ID)
                        logger.info("Processing message with correlationId: $correlationId")

                        // Xử lý message với reactive operators
                        processMessage(msg)
                    }
                }.then()
        }

    private fun processMessage(message: String): Mono<Void> =
        fromRunnable<Void> {
            logger.info(("Processing message: $message"))
        }.subscribeOn(Schedulers.boundedElastic())
}
