// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.rxevent.cor

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.rxevent.RxEventHandler
import com.vjcspy.rxevent.assertPayload
import com.vjcspy.rxevent.ofAction
import com.vjcspy.spring.base.annotation.rxevent.Effect
import com.vjcspy.spring.packages.stocksync.dto.vietstock.CorporateData
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
                .ofAction(CorAction.COR_LOAD_NEXT_PAGE_ACTION)
                .map {
                    val payload = it.assertPayload<CorLoadNextPagePayload>()

                    payload
                }.concatMapSingle {
                    logger.info("start load cor data for page ${it.currentPage + 1}")
                    corService.getCorporateData(page = it.currentPage + 1).map { t ->
                        listOf(it, t)
                    }
                }.map { data ->
                    val payload = data[0] as CorLoadNextPagePayload
                    val corData = data[1] as? List<CorporateData>

                    if (corData == null) {
                        return@map CorAction.COR_LOAD_NEXT_PAGE_ERROR_ACTION(
                            CorLoadNextPageErrorPayload(
                                errorMessage = "Wrong data from downstream",
                                error = Exception("Wrong data from downstream"),
                            ),
                        )
                    }

                    logger.info("successfully get corporate data")
                    if (corData.isEmpty()) {
                        if (payload.currentPage > 0) {
                            logger.info("Loaded full data")
                        }

                        return@map CorAction.COR_LOAD_COMPLETE_ACTION(null)
                    }

                    CorAction.COR_LOAD_NEXT_PAGE_SUCCESS_ACTION(
                        CorLoadNexPageSuccessPayload(
                            page = payload.currentPage + 1,
                            data = corData,
                        ),
                    )
                }
        }

    @Effect
    fun handleLoadPageSuccess(): RxEventHandler =
        RxEventHandler { action ->
            action
                .ofAction(CorAction.COR_LOAD_NEXT_PAGE_SUCCESS_ACTION)
                .map {
                    val payload = it.assertPayload<CorLoadNexPageSuccessPayload>()
                    CorAction.COR_LOAD_NEXT_PAGE_ACTION(CorLoadNextPagePayload(currentPage = payload.page))
                }
        }
}
