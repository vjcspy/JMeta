package com.vjcspy.spring.base.context

import MDCContextLifter
import com.vjcspy.kotlinutilities.log.KtLogging
import org.jetbrains.annotations.NotNull
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.event.ContextRefreshedEvent
import reactor.core.publisher.Hooks
import reactor.core.publisher.Operators

@Configuration
@EnableAspectJAutoProxy
open class ContextConfiguration : ApplicationListener<ContextRefreshedEvent> {
    companion object {
        private const val MDC_CONTEXT_REACTOR_KEY = "mdc-context-reactor-key"
    }

    private val logger = KtLogging.logger {}

    override fun onApplicationEvent(
        @NotNull event: ContextRefreshedEvent,
    ) {
        logger.info("Initializing ReactiveContextHook")
        Hooks.onEachOperator(
            MDC_CONTEXT_REACTOR_KEY,
            Operators.lift { _, subscriber ->
                MDCContextLifter(subscriber)
            },
        )
    }
}
