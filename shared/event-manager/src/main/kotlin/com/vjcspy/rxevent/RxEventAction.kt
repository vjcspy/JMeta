package com.vjcspy.rxevent

import java.util.*

data class RxEventAction<T>(
    val type: String,
    val payload: T? = null,
) {
    var correlationId: UUID? = null
        set(value) {
            check(value != null) {
                "Correlation ID cannot be null"
            }
            check(field == null) {
                "Attempted to set correlationId when it already exists"
            }

            field = value
        }
}
