// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.rxevent.cor

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.rxevent.RxEventHandler
import com.vjcspy.rxevent.assertPayload
import com.vjcspy.rxevent.ofAction
import com.vjcspy.spring.base.annotation.rxevent.Effect
import com.vjcspy.spring.packages.stocksync.service.CorService
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class CorEffect(
    private val corService: CorService,
) {
    private val logger = KtLogging.logger()

    @Effect
    fun handleUserEvents(): RxEventHandler =
        RxEventHandler { action ->
            action
                .ofAction(arrayOf(CorAction.COR_LOAD_NEXT_PAGE_ACTION))
                .map {
                    val payload = it.assertPayload<CorLoadNextPagePayload>()
                    logger.info("payload ${payload.currentPage}")
                    CorAction.COR_LOAD_NEXT_PAGE_SUCCESS_ACTION(null)
                }
        }
}
