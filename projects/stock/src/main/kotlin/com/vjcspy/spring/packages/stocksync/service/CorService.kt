/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.vjcspy.spring.packages.stocksync.dto.vietstock.VietStockCredential
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CorService(private val vietStockCredentialService: VietStockCredentialService) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private val objectMapper = ObjectMapper()
    }

    fun getCorporateData(page: Int): List<Map<String, Any>>? = runBlocking {
        try {
            val credential = vietStockCredentialService.retrieveCredentials()

            val response = vietStockCredentialService.client.post("https://finance.vietstock.vn/data/corporateaz") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(buildRequestBody(page, credential.csrf))

                header("Referer", "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1")
                header("Referrer-Policy", "strict-origin-when-cross-origin")
                header("Cookie", buildCookieHeader(credential))
            }

            if (response.status.isSuccess()) {
                objectMapper.readValue(
                    response.bodyAsText(),
                    object : TypeReference<List<Map<String, Any>>>() {},
                )
            } else {
                logger.error("Failed to get corporate data. Status: ${response.status}")
                null
            }
        } catch (ex: Exception) {
            logger.error("Error getting Corporate data", ex)
            null
        }
    }

    private fun buildRequestBody(page: Int, csrf: String): String = buildString {
        append("catID=0")
        append("&industryID=0")
        append("&page=$page")
        append("&pageSize=10")
        append("&type=1")
        append("&code=")
        append("&businessTypeID=0")
        append("&orderBy=Code")
        append("&orderDir=ASC")
        append("&__RequestVerificationToken=$csrf")
    }

    private fun buildCookieHeader(credential: VietStockCredential): String = buildString {
        append("ASP.NET_SessionId=${credential.sid}")
        append("; __RequestVerificationToken=${credential.rvt}")
        append("; language=vi-VN")
        append("; Theme=Light")
        append("; isShowLogin=true")
        append("; vts_usr_lg=${credential.vtsUsrLg}")
        append("; vst_usr_lg_token=${credential.usrTk}")
    }
}
