// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.rxevent.cor

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.rxevent.RxEventHandler
import com.vjcspy.rxevent.ofAction
import com.vjcspy.spring.base.annotation.rxevent.Effect
import com.vjcspy.spring.tedbed.rxeventmanager.TestBedAction
import net.logstash.logback.argument.StructuredArguments.value
import org.springframework.stereotype.Component

@Component
class CoreEffect {
    private val logger = KtLogging.logger()

    @Effect
    fun handleUserEvents(): RxEventHandler =
        RxEventHandler { action ->
            action.ofAction(arrayOf(CorAction.COR_LOAD_NEXT_PAGE_ACTION)).map {
                logger.info(
                    "Action type {} processed for payload {}",
                    value("type", it.type),
                    value("payload", it.payload),
                )
                TestBedAction.BAR_ACTION(null)
            }
        }
}
