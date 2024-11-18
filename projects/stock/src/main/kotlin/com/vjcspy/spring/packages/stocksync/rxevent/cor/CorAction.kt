// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.rxevent.cor

import com.vjcspy.rxevent.rxEventActionFactory

object CorAction {
    val COR_LOAD_NEXT_PAGE_ACTION = rxEventActionFactory("COR_LOAD_NEXT_PAGE")
    val COR_LOAD_NEXT_PAGE_SUCCESS_ACTION = rxEventActionFactory("COR_LOAD_NEXT_PAGE_SUCCESS_ACTION")
    val COR_LOAD_NEXT_PAGE_ERROR_ACTION = rxEventActionFactory("COR_LOAD_NEXT_PAGE_ERROR_ACTION")
}
