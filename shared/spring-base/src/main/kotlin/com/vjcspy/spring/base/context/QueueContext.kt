package com.vjcspy.spring.base.context

import java.util.*
import org.slf4j.MDC
import reactor.util.context.Context

class QueueContext private constructor() {
    companion object {
        private val contextHolder = ThreadLocal<String>()

        fun initContext(): Context {
            val correlationId = UUID.randomUUID().toString()
            contextHolder.set(correlationId)
            MDC.put(ApplicationContext.CORRELATION_ID, correlationId)
            return Context.of(ApplicationContext.CORRELATION_ID, correlationId)
        }

        fun getCorrelationId(): String? = contextHolder.get()

        fun hasContext(): Boolean = contextHolder.get() != null

        fun clearContext() {
            contextHolder.remove()
            MDC.remove(ApplicationContext.CORRELATION_ID)
        }
    }
}
