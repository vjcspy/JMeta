package com.vjcspy.spring.tedbed.fluxevent

import com.vjcspy.fluxevent.FluxEventHandler
import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.rxevent.EMPTY_ACTION
import com.vjcspy.spring.base.annotation.rxevent.Effect
import com.vjcspy.spring.tedbed.rxeventmanager.TestBedAction
import org.springframework.stereotype.Component

@Component
class TedBedFluxEffect {
    val logger = KtLogging.logger {}

    @Effect(types = [TestBedAction.FOO_TYPE])
    fun handleUserEvents(): FluxEventHandler =
        { upstream ->
            upstream.map {
                logger.info("Process event ${TestBedAction.FOO_TYPE}")
                TestBedAction.BAR_ACTION(null)
            }
        }

    @Effect(types = [TestBedAction.BAR_TYPE])
    fun handleUserEvents2(): FluxEventHandler =
        { upstream ->
            upstream.map {
                logger.info("Process event ${TestBedAction.BAR_TYPE}")

                EMPTY_ACTION(null)
            }
        }
}
