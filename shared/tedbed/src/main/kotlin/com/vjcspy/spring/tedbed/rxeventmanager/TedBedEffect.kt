package com.vjcspy.spring.tedbed.rxeventmanager

import com.vjcspy.rxevent.RxEventHandler
import com.vjcspy.spring.base.annotation.rxevent.Effect
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class TedBedEffect {

    @Effect(types = [TestBedAction.FOO_TYPE])
    fun handleUserEvents(): RxEventHandler {
        return RxEventHandler { upstream ->
            upstream.map {
                logger.info { "Processing user event: ${it.type}" }
                TestBedAction.BAR_ACTION(null)
            }
        }
    }
}
