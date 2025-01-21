// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.rxevent.cor

import com.vjcspy.rxevent.eventActionFactory
import com.vjcspy.spring.packages.stocksync.dto.vietstock.CorporateData

data class CorLoadNextPagePayload(
    val currentPage: Int,
)

data class CorLoadNexPageSuccessPayload(
    val page: Int,
    val data: List<CorporateData>,
)

data class CorLoadNextPageErrorPayload(
    val errorMessage: String? = null,
    val error: Throwable,
)

object CorAction {
    val COR_LOAD_NEXT_PAGE_ACTION =
        eventActionFactory<CorLoadNextPagePayload>("COR_LOAD_NEXT_PAGE_ACTION")
    val COR_LOAD_NEXT_PAGE_SUCCESS_ACTION =
        eventActionFactory<CorLoadNexPageSuccessPayload>("COR_LOAD_NEXT_PAGE_SUCCESS_ACTION")
    val COR_LOAD_NEXT_PAGE_ERROR_ACTION =
        eventActionFactory<CorLoadNextPageErrorPayload>("COR_LOAD_NEXT_PAGE_ERROR_ACTION")
    val COR_LOAD_COMPLETE_ACTION = eventActionFactory<Nothing>("COR_LOAD_COMPLETE_ACTION")
}
