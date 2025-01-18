package com.vjcspy.spring.base.context

import com.vjcspy.kotlinutilities.log.KtLogging
import java.util.*
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Aspect
@Component
@Order(1)
class QueueContextAspect {
    private val logger = KtLogging.logger {}

    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    fun aroundRabbitListener(joinPoint: ProceedingJoinPoint): Any {
        try {
            // Extract context from message or create new
            val contextData = extractContextData(joinPoint.args)

            val result = joinPoint.proceed()
            if (result !is Mono<*>) {
                return result
            }

            return result
                .contextWrite { ctx ->
                    var newCtx = ctx
                    contextData.forEach { (key, value) ->
                        newCtx = newCtx.put(key, value)
                    }
                    newCtx
                }.doFinally {
                    MDC.clear()
                }
        } catch (e: Exception) {
            logger.error("Error in RabbitListener aspect", e)
            throw e
        }
    }

    private fun extractContextData(args: Array<Any>): Map<String, String> {
        val contextData = mutableMapOf<String, String>()

        // Default correlation ID if none found
        var correlationId: String? = null

        // Try to find correlation ID in message headers
//        for (arg in args) {
//            when (arg) {
//                is Message -> {
//                    correlationId = extractCorrelationId(arg.messageProperties)
//                }
//                is MessageHeaders -> {
//                    correlationId = arg["x-correlation-id"]?.toString()
//                }
//            }
//
//            if (correlationId != null) break
//        }

        // If no correlation ID found in headers, generate new one
        contextData[CORRELATION_ID_KEY] = correlationId
            ?: UUID.randomUUID().toString()

        // Add more context data extraction here if needed
        // contextData["other-key"] = extractOtherData(args)

        return contextData
    }

//    private fun extractCorrelationId(properties: MessageProperties): String? =
//        properties.headers["x-correlation-id"]?.toString()
//            ?: properties.correlationId
}
