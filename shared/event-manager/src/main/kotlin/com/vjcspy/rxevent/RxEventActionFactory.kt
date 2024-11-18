package com.vjcspy.rxevent

interface EventActionFactory<out T> : (@UnsafeVariance T?) -> RxEventAction<@UnsafeVariance T?> {
    val type: String
}

// Hàm factory tạo EventActionFactory
fun <T> rxEventActionFactory(type: String): EventActionFactory<T?> =
    object : EventActionFactory<T?> {
        override val type: String = type

        override fun invoke(payload: T?): RxEventAction<T?> = RxEventAction(type, payload)
    }
