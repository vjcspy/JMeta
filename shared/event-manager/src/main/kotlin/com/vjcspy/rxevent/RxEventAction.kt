package com.vjcspy.rxevent

import org.slf4j.LoggerFactory
import java.util.*

data class RxEventAction(val type: String, val payload: Any? = null) {
    private val log = LoggerFactory.getLogger(RxEventAction::class.java)

    var correlationId: UUID? = null
        set(value: UUID?) {
            if (value == null) {
                log.error("Attempted to set correlationId to null")
                throw NullPointerException("Correlation ID cannot be null")
            }

            if (field == null) {
                field = value
            } else {
                log.error("Attempted to set correlationId when it already exists")
                throw IllegalStateException("Correlation ID already exists")
            }
        }
}
