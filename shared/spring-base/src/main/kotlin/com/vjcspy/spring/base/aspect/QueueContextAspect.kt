package com.vjcspy.spring.base.aspect
import com.vjcspy.spring.base.context.ApplicationContext
import com.vjcspy.spring.base.context.QueueContext
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.Context

@Aspect
@Component
@Order(1) // Đảm bảo aspect này chạy trước các aspect khác
class QueueContextAspect {
    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    fun aroundQueueConsumer(joinPoint: ProceedingJoinPoint): Any? =
        try {
            val reactorContext = QueueContext.initContext()
            val result = joinPoint.proceed()

            when (result) {
                is Mono<*> -> handleMono(result, reactorContext)
                is Flux<*> -> handleFlux(result, reactorContext)
                else -> result
            }
        } finally {
            QueueContext.clearContext()
        }

    private fun <T> handleMono(
        mono: Mono<T>,
        context: Context,
    ): Mono<T> =
        mono
            .contextWrite(context)
            .doOnEach { signal ->
                if (!signal.isOnComplete && !signal.isOnError) {
                    signal
                        .contextView
                        .getOrEmpty<String>(ApplicationContext.CORRELATION_ID)
                        .ifPresent { MDC.put(ApplicationContext.CORRELATION_ID, it) }
                }
            }.doFinally { MDC.clear() }

    private fun <T> handleFlux(
        flux: Flux<T>,
        context: Context,
    ): Flux<T> =
        flux
            .contextWrite(context)
            .doOnEach { signal ->
                if (!signal.isOnComplete && !signal.isOnError) {
                    signal
                        .contextView
                        .getOrEmpty<String>(ApplicationContext.CORRELATION_ID)
                        .ifPresent { MDC.put(ApplicationContext.CORRELATION_ID, it) }
                }
            }.doFinally { MDC.clear() }
}
