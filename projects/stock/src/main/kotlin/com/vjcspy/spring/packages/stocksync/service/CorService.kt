// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.service

import com.vjcspy.spring.packages.stocksync.dto.vietstock.CorporateData
import com.vjcspy.spring.packages.stocksync.dto.vietstock.VietStockCredential
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.rx3.rxSingle
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class CorService(
    private val vietStockCredentialService: VietStockCredentialService,
) {
    private val logger = KotlinLogging.logger {}

    fun getCorporateData(page: Int): Single<List<CorporateData>> {
        return rxSingle {
            try {
                val credential = vietStockCredentialService.retrieveCredentials()

                val response =
                    vietStockCredentialService.client.post("https://finance.vietstock.vn/data/corporateaz") {
                        contentType(ContentType.Application.FormUrlEncoded)
                        setBody(buildRequestBody(page, credential.csrf))

                        header("Referer", "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1")
                        header("Referrer-Policy", "strict-origin-when-cross-origin")
                        header("Cookie", buildCookieHeader(credential))
                    }

                if (response.status.isSuccess()) {
                    return@rxSingle Json.decodeFromString<List<CorporateData>>(response.bodyAsText())
                } else {
                    throw Exception("Failed to fetch data, status: ${response.status}")
                }
            } catch (ex: Exception) {
                logger.error(ex) { "Error getting Corporate data" }
                throw ex
            }
        }
    }

    private fun buildRequestBody(
        page: Int,
        csrf: String,
    ): String =
        buildString {
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

    private fun buildCookieHeader(credential: VietStockCredential): String =
        buildString {
            append("ASP.NET_SessionId=${credential.sid}")
            append("; __RequestVerificationToken=${credential.rvt}")
            append("; language=vi-VN")
            append("; Theme=Light")
            append("; isShowLogin=true")
            append("; vts_usr_lg=${credential.vtsUsrLg}")
            append("; vst_usr_lg_token=${credential.usrTk}")
        }
}
