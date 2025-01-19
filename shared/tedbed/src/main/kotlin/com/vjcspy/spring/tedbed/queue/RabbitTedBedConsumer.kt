package com.vjcspy.spring.tedbed.queue

import com.rabbitmq.client.Channel
import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.spring.base.context.CORRELATION_ID_KEY
import com.vjcspy.spring.tedbed.util.DelayHttpRequest
import org.slf4j.MDC
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.fromRunnable
import reactor.core.scheduler.Schedulers

@Component
class RabbitTedBedConsumer {
    private val logger = KtLogging.logger {}

    @RabbitListener(
        queues = [RabbitTedBedConfig.EXAMPLE_QUEUE],
        containerFactory = "tedBedListenerContainerFactory",
    )
    fun consumeMessage(
        message: String,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long,
    ) = Mono.defer {
        Mono
            .just(message)
            .flatMap { msg ->
                Mono.deferContextual { ctx ->
                    val reactiveCorrelationId = ctx.get<String>(CORRELATION_ID_KEY)
                    val mdcCorrelation = MDC.get(CORRELATION_ID_KEY)
                    logger.info("before correlationId: $reactiveCorrelationId")
                    if (reactiveCorrelationId != mdcCorrelation) {
                        logger.error(
                            "CorrelationId not match before request $reactiveCorrelationId != $mdcCorrelation",
                        )
                    }

                    DelayHttpRequest
                        .makeDelayHttpRequest()
                        .flatMap { response ->
                            val afterReactiveCorrelationId = ctx.get<String>(CORRELATION_ID_KEY)
                            val afterMdcCorrelation = MDC.get(CORRELATION_ID_KEY)
                            logger.info("Response from slow-api: $response")
                            logger.info("after correlationId: $afterMdcCorrelation")
                            if (afterReactiveCorrelationId != afterMdcCorrelation) {
                                logger.error(
                                    "CorrelationId not match after request $reactiveCorrelationId != $mdcCorrelation",
                                )
                            }

                            if (reactiveCorrelationId != afterReactiveCorrelationId) {
                                logger.error(
                                    "CorrelationId not match before and after request $reactiveCorrelationId != $afterReactiveCorrelationId",
                                )
                            }

                            processMessage(response)
                        }
                }
            }.then()
    }

    private fun processMessage(message: String): Mono<Void> =
        // Must use deferContextual to get the context from the subscriber
        Mono.deferContextual { ctx ->
            val correlationId = ctx.get<String>(CORRELATION_ID_KEY)
            fromRunnable<Void> {
                logger.info("Processing message: $message with correlationId: $correlationId")
            }.subscribeOn(Schedulers.boundedElastic())
        }
}
