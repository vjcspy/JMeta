package com.vjcspy.rxevent

import java.util.*

data class RxEventAction<out T>(
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

inline fun <reified T> RxEventAction<*>.assertPayload(): T {
    check(this.payload is T) {
        "Payload is not of type ${T::class.simpleName}, but is of type ${
            payload?.let {
                it::class.simpleName
            } ?: "null"
        }"
    }

    return this.payload
}
