package com.vjcspy.kotlinutilities.log

import kotlin.reflect.KClass
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun getLogger(clazz: KClass<*>): Logger = LoggerFactory.getLogger(clazz.java)

object KtLogging {
    fun logger(): Logger {
        val caller =
            StackWalker
                .getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk { frames ->
                    frames
                        .map { it.declaringClass }
                        .filter { it != KtLogging::class.java }
                        .findFirst()
                        .get()
                }

        return LoggerFactory.getLogger(caller)
    }

    /**
    * kotlin Trailing Lambda style
    * */
    fun logger(x: () -> Unit): Logger = this.logger()
}
