package com.vjcspy.spring.packages.stocksync.mapper

import com.vjcspy.spring.packages.stockinfo.entity.CorEntity
import com.vjcspy.spring.packages.stocksync.dto.vietstock.CorporateData

fun mapToCorEntity(corporateData: CorporateData): CorEntity =
    CorEntity(
        catId = corporateData.CatID,
        code = corporateData.Code,
        exchange = corporateData.Exchange,
        industryName1 = corporateData.IndustryName1,
        industryName2 = corporateData.IndustryName2,
        industryName3 = corporateData.IndustryName3,
        totalShares = corporateData.TotalShares.toBigInteger(),
        name = corporateData.Name,
        firstTradeDate = corporateData.FirstTradeDate,
    )
