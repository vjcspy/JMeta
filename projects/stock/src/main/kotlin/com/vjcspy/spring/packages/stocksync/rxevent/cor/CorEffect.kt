// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.rxevent.cor

import com.vjcspy.kotlinutilities.log.KtLogging
import com.vjcspy.rxevent.RxEventAction
import com.vjcspy.rxevent.RxEventHandler
import com.vjcspy.rxevent.assertPayload
import com.vjcspy.rxevent.ofAction
import com.vjcspy.spring.base.annotation.rxevent.Effect
import com.vjcspy.spring.packages.stockinfo.service.CorEntityService
import com.vjcspy.spring.packages.stocksync.dto.vietstock.CorporateData
import com.vjcspy.spring.packages.stocksync.mapper.mapToCorEntity
import com.vjcspy.spring.packages.stocksync.service.CorService
import io.reactivex.rxjava3.schedulers.Schedulers
import org.springframework.stereotype.Component

@Suppress("unused")
@Component
class CorEffect(
    private val corService: CorService,
    private val corEntityService: CorEntityService,
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
                }.observeOn(Schedulers.io())
                .map {
                    if (it.currentPage == 0) {
                        corEntityService.deleteAllCorEntities()
                    }

                    it
                }.concatMapSingle {
                    logger.info("start load cor data for page ${it.currentPage + 1}")
                    corService.getCorporateData(page = it.currentPage + 1).map { t ->
                        listOf(it, t)
                    }
                }.map { data ->
                    val payload = data[0] as CorLoadNextPagePayload
                    val corData =
                        data[1] as? List<*>
                            ?: return@map CorAction.COR_LOAD_NEXT_PAGE_ERROR_ACTION(
                                CorLoadNextPageErrorPayload(
                                    errorMessage = "Wrong data from downstream",
                                    error = Exception("Wrong data from downstream"),
                                ),
                            )

                    logger.info("Successfully retrieved corporate data.")

                    // Kiểm tra và chuyển đổi danh sách thành List<CorporateData> an toàn
                    val corporateDataList = corData.filterIsInstance<CorporateData>()

                    if (corporateDataList.isEmpty()) {
                        if (payload.currentPage > 0) {
                            logger.info("Loaded full data")
                        }

                        return@map CorAction.COR_LOAD_COMPLETE_ACTION(null)
                    }

                    CorAction.COR_LOAD_NEXT_PAGE_SUCCESS_ACTION(
                        CorLoadNexPageSuccessPayload(
                            page = payload.currentPage + 1,
                            data = corporateDataList,
                        ),
                    )
                }
        }

    @Effect
    fun handleLoadPageSuccess(): RxEventHandler =
        RxEventHandler { action ->
            action
                .ofAction(CorAction.COR_LOAD_NEXT_PAGE_SUCCESS_ACTION)
                .observeOn(Schedulers.io())
                .map {
                    val payload = it.assertPayload<CorLoadNexPageSuccessPayload>()
                    corEntityService.saveCorEntities(
                        payload.data.map {
                            mapToCorEntity(it)
                        },
                    )
                    logger.info("Successfully save corporate data")
                    CorAction.COR_LOAD_NEXT_PAGE_ACTION(
                        CorLoadNextPagePayload(currentPage = payload.page),
                    ) as RxEventAction<Any?>
                }.onErrorReturn { error ->
                    logger.error("Error when handle save corporation data", error)
                    CorAction.COR_LOAD_NEXT_PAGE_ERROR_ACTION(
                        CorLoadNextPageErrorPayload(
                            errorMessage = "Error when handle save corporation data",
                            error = error,
                        ),
                    )
                }
        }
}
