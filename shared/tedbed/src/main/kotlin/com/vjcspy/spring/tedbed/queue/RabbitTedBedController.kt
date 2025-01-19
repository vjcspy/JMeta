package com.vjcspy.spring.tedbed.queue

import com.vjcspy.kotlinutilities.log.KtLogging
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/tedbed/queue")
class RabbitTedBedController(
    private val rabbitTedBedPublisher: RabbitTedBedPublisher,
) {
    private val logger = KtLogging.logger {}

    @RequestMapping("/test")
    fun test(): Mono<Map<String, String>> {
        repeat(3) {
            rabbitTedBedPublisher.sendMessage("$it")
        }
        return Mono.just(mapOf("status" to "ok"))
    }

    @RequestMapping("/test1")
    fun test1(): Mono<Map<String, String>> =
        Mono
            .just("Hello")
            .contextWrite { ctx ->
                ctx.put("correlationId", "correlationId-3")
            }.flatMap { msg ->
                Mono.deferContextual { ctx ->
                    // Sẽ in ra: correlationId-2
                    logger.info("First read: ${ctx.get<String>("correlationId")}")
                    Mono.just(msg)
                }
            }.contextWrite { ctx ->
                ctx.put("correlationId", "correlationId-2")
            }.flatMap { msg ->
                Mono.deferContextual { ctx ->
                    // Sẽ in ra: correlationId-1
                    logger.info("Second read: ${ctx.get<String>("correlationId")}")
                    Mono.just(msg)
                }
            }.contextWrite { ctx ->
                ctx.put("correlationId", "correlationId-1")
            }.map {
                mapOf("status" to "ok11111")
            }
}
