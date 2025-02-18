package com.vjcspy.rxevent

import com.vjcspy.kotlinutilities.log.KtLogging
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.UUID

class RxEventManager private constructor() {
    companion object {
        val actionSubject: PublishSubject<EventAction<Any?>> = PublishSubject.create()
        private val logger = KtLogging.logger {}

        fun dispatch(action: EventAction<Any?>) {
            logger.info("Dispatching action ${action.type}")
            if (action.correlationId == null) {
                action.correlationId = UUID.randomUUID()
            }
            actionSubject.onNext(action)
        }

        fun registerEvent(
            eventTypes: Array<String>,
            eventHandler: RxEventHandler,
        ) {
            actionSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .filter { eventAction -> if (eventTypes.isEmpty()) true else eventTypes.contains(eventAction.type) }
                .flatMap { originalEvent ->
                    Observable
                        .just(originalEvent)
                        .compose(eventHandler)
                        .map { handledEvent -> listOf(originalEvent, handledEvent) }
                }.subscribeBy(
                    onNext = { events ->
                        val originEvent = events[0]
                        val handledEvent = events[1]

                        require(originEvent is EventAction) { "originEvent is not an instance of RxEventAction" }
                        require(handledEvent is EventAction) { "handledEvent is not an instance of RxEventAction" }

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
                    onError = { error ->
                        logger.error("Error in event stream: ${error.message}")
                    },
                    onComplete = {
                        logger.error("Why Completed???")
                    },
                )
        }
    }
}
