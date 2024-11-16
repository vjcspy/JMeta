package com.vjcspy.rxevent

import org.slf4j.LoggerFactory
import java.util.*

data class RxEventAction(val type: String, val payload: Any? = null) {
    private val log = LoggerFactory.getLogger(RxEventAction::class.java)

    var correlationId: UUID? = null
        set(value) {
            check(value == null) {
                "Correlation ID cannot be null"
            }
            check(field != null) {
                "Attempted to set correlationId when it already exists"
            }

            field = value
        }
}
