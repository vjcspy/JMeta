package com.vjcspy.rxevent

interface EventActionFactory<out T> : (@UnsafeVariance T?) -> EventAction<@UnsafeVariance T?> {
    val type: String
}

// Hàm factory tạo EventActionFactory
fun <T> eventActionFactory(type: String): EventActionFactory<T?> =
    object : EventActionFactory<T?> {
        override val type: String = type

        override fun invoke(payload: T?): EventAction<T?> = EventAction(type, payload)
    }
