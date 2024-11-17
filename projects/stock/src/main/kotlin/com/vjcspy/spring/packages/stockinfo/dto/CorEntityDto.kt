// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stockinfo.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigInteger
import java.time.LocalDate

data class CorEntityDto(
    val id: Int? = null,
    val refId: Int? = null,
    val catId: Int? = null,
    val code: String? = null,
    val exchange: String? = null,
    val industryName1: String? = null,
    val industryName2: String? = null,
    val industryName3: String? = null,
    val totalShares: BigInteger? = null,
    val name: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    val firstTradeDate: LocalDate? = null,
)
