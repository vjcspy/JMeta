package com.vjcspy.spring.base.config

import com.vjcspy.fluxevent.FluxEventHandler
import com.vjcspy.fluxevent.FluxEventManager
import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.rxevent.RxEventHandler
import com.vjcspy.rxevent.RxEventManager
import com.vjcspy.spring.base.annotation.rxevent.Effect
import io.reactivex.rxjava3.core.ObservableTransformer
import java.lang.reflect.Method
import org.jetbrains.annotations.NotNull
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.util.ReflectionUtils

@Configuration
open class EventManagerConfiguration {
    @Bean
    open fun eventHandlerInitializer(): EventHandlerInitializer = EventHandlerInitializer()

    class EventHandlerInitializer : ApplicationListener<ContextRefreshedEvent> {
        private val logger = KtLogging.logger { }

        companion object {
            private var initialized = false
        }

        override fun onApplicationEvent(
            @NotNull event: ContextRefreshedEvent,
        ) {
            if (!initialized) {
                val applicationContext = event.applicationContext
                registerEventHandlers(applicationContext)
                initialized = true
            }
        }

        private fun registerEventHandlers(applicationContext: ApplicationContext) {
            logger.info("Registering Event Handlers for all Effects")
            val beanNames = applicationContext.beanDefinitionNames
            for (beanName in beanNames) {
                try {
                    val bean = applicationContext.getBean(beanName)
                    ReflectionUtils.doWithMethods(
                        bean.javaClass,
                        { method -> registerEventHandler(bean, method) },
                        { method -> method.isAnnotationPresent(Effect::class.java) },
                    )
                } catch (e: Exception) {
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun registerEventHandler(
            bean: Any,
            method: Method,
        ) {
            try {
                val annotation = method.getAnnotation(Effect::class.java) ?: return

                method.isAccessible = true
                val handler = method.invoke(bean)

                // Sử dụng 'is' để check type
                if (handler is ObservableTransformer<*, *>) {
                    // Check thêm generic type thông qua method return type
                    val returnType = method.genericReturnType.toString()
                    if (returnType.contains("EventAction")) {
                        // Safe cast sau khi đã check
                        val eventHandler = handler

                        val eventTypes = annotation.types

                        @Suppress("UNCHECKED_CAST")
                        RxEventManager.registerEvent(eventTypes, eventHandler as RxEventHandler)

                        logger.info(
                            "Registered RX event handler for types: ${eventTypes.joinToString()} " +
                                "from bean: ${bean.javaClass.simpleName}",
                        )
                    } else {
                        logger.warn(
                            "Method ${method.name} in ${bean.javaClass.simpleName} " +
                                "returns ObservableTransformer but with wrong generic types",
                        )
                    }
                } else if (handler is Function1<*, *>) {
                    // Đây là sử dụng Flux
                    val returnType = method.genericReturnType.toString()
                    if (returnType.contains("EventAction")) {
                        // Safe cast sau khi đã check
                        val eventHandler = handler

                        val eventTypes = annotation.types

                        @Suppress("UNCHECKED_CAST")
                        FluxEventManager.registerEvent(eventTypes, eventHandler as FluxEventHandler)

                        logger.info(
                            "Registered Flux event handler for types: ${eventTypes.joinToString()} " +
                                "from bean: ${bean.javaClass.simpleName}",
                        )
                    } else {
                        logger.warn(
                            "Method ${method.name} in ${bean.javaClass.simpleName} " +
                                "returns Flux but with wrong generic types",
                        )
                    }
                } else {
                    logger.warn(
                        "Method ${method.name} in ${bean.javaClass.simpleName} " +
                            "does not return ObservableTransformer or Flux (actual type: ${handler?.javaClass})",
                    )
                }
            } catch (e: Exception) {
                logger.error(
                    "Failed to register event handler for method: ${method.name} " +
                        "in bean: ${bean.javaClass.simpleName}",
                    e,
                )
            }
        }
    }
}
