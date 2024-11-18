// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.rxevent.cor

import com.vjcspy.rxevent.rxEventActionFactory

data class CorLoadNextPagePayload(
    val currentPage: Int,
)

data class CorLoadNextPageErrorPayload(
    val errorMessage: String? = null,
    val error: Throwable,
)

object CorAction {
    val COR_LOAD_NEXT_PAGE_ACTION =
        rxEventActionFactory<CorLoadNextPagePayload>("COR_LOAD_NEXT_PAGE_ACTION")
    val COR_LOAD_NEXT_PAGE_SUCCESS_ACTION =
        rxEventActionFactory<Any?>("COR_LOAD_NEXT_PAGE_SUCCESS_ACTION")
    val COR_LOAD_NEXT_PAGE_ERROR_ACTION =
        rxEventActionFactory<CorLoadNextPageErrorPayload>("COR_LOAD_NEXT_PAGE_ERROR_ACTION")
}
