package com.vjcspy.fluxevent

import com.vjcspy.rxevent.EventAction
import reactor.core.publisher.Flux

typealias FluxEventHandler = (Flux<EventAction<Any?>>) -> Flux<EventAction<Any?>>
