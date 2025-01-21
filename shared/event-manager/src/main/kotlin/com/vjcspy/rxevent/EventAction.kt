package com.vjcspy.rxevent

import java.util.*

data class EventAction<out T>(
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

inline fun <reified T> EventAction<*>.assertPayload(): T {
    check(this.payload is T) {
        "Payload is not of type ${T::class.simpleName}, but is of type ${
            payload?.let {
                it::class.simpleName
            } ?: "null"
        }"
    }

    return this.payload
}

val EMPTY_ACTION = eventActionFactory<Nothing>("EMPTY_ACTION")
