package com.vjcspy.fluxevent

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.rxevent.EventAction
import java.util.UUID
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.scheduler.Schedulers

class FluxEventManager private constructor() {
    companion object {
        private val actionSink: Sinks.Many<EventAction<Any?>> = Sinks.many().multicast().onBackpressureBuffer()
        private val actionFlux: Flux<EventAction<Any?>> = actionSink.asFlux()
        private val logger = KtLogging.logger()

        fun dispatch(action: EventAction<Any?>) {
            logger.info("Dispatching action ${action.type}")
            if (action.correlationId == null) {
                action.correlationId = UUID.randomUUID()
            }
            // Thay onNext bằng tryEmitNext
            actionSink
                .tryEmitNext(action)
                .orThrow() // Xử lý emission failure
        }

        fun registerEvent(
            eventTypes: Array<String>,
            eventHandler: FluxEventHandler,
        ) {
            actionFlux
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .filter { eventAction ->
                    if (eventTypes.isEmpty()) true else eventTypes.contains(eventAction.type)
                }.flatMap { originalEvent ->
                    Flux
                        .just(originalEvent)
                        .transform(eventHandler)
                        .map { handledEvent -> listOf(originalEvent, handledEvent) }
                }.subscribe(
                    { events ->
                        val originEvent = events[0]
                        val handledEvent = events[1]

                        require(originEvent is EventAction) { "originEvent is not an instance of EventAction" }
                        require(handledEvent is EventAction) { "handledEvent is not an instance of EventAction" }

                        check(
                            !(
                                originEvent.correlationId != null &&
                                    handledEvent.correlationId != null &&
                                    originEvent.correlationId != handledEvent.correlationId
                            ),
                        ) {
                            "Origin Event và Handled Event không cùng correlationId"
                        }

                        if (originEvent.correlationId != null && handledEvent.correlationId == null) {
                            handledEvent.correlationId = originEvent.correlationId
                        }

                        @Suppress("UNCHECKED_CAST")
                        dispatch(handledEvent as EventAction<Any>)
                    },
                    { error ->
                        logger.error("Error in event stream: ${error.message}")
                    },
                    {
                        logger.error("Why Completed???")
                    },
                )
        }
    }
}
