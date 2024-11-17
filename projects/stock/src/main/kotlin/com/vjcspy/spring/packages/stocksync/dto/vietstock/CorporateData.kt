package com.vjcspy.spring.packages.stocksync.dto.vietstock

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable

private val logger = KotlinLogging.logger {}

@Serializable
data class CorporateData(
    val ID: Int,
    val CatID: Int,
    val Exchange: String,
    val IndustryName1: String,
    val IndustryName2: String,
    val IndustryName3: String,
    val Code: String,
    val Name: String,
    val TotalShares: Long,
    val URL: String,
    val Row: Int,
    val FirstTradeDate: String, // Có thể chuyển về kiểu `LocalDateTime` nếu cần
    val TotalRecord: Int,
)
