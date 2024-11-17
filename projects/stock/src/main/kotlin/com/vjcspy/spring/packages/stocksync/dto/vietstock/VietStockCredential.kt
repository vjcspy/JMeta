/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.dto.vietstock

data class VietStockCredential(
    val sid: String,
    val rvt: String,
    val vtsUsrLg: String,
    val usrTk: String,
    val csrf: String,
)
