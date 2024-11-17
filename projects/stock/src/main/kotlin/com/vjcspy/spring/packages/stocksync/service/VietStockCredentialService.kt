/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.service

import com.vjcspy.spring.packages.stocksync.dto.vietstock.VietStockCredential
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VietStockCredentialService {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var cachedCredentials: VietStockCredential? = null

    val client = HttpClient(CIO) {
        install(UserAgent) {
            agent =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36"
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
        }
        defaultRequest {
            header("Accept", "*/*")
            header("Accept-Language", "vi,en-US;q=0.9,en;q=0.8")
            header("Cache-Control", "no-cache")
            header("Pragma", "no-cache")
            header("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"97\", \"Chromium\";v=\"97\"")
            header("sec-ch-ua-mobile", "?0")
            header("sec-ch-ua-platform", "\"macOS\"")
            header("sec-fetch-dest", "empty")
            header("sec-fetch-mode", "cors")
            header("sec-fetch-site", "same-origin")
            header("x-requested-with", "XMLHttpRequest")
        }
    }

    fun retrieveCredentials(): VietStockCredential = runBlocking {
        cachedCredentials?.let { return@runBlocking it }

        val loggedCrds = loggedCookies()
        val cookiePattern =
            Regex(
                "(.*)(ASP.NET_SessionId=.*;)(.*)(__RequestVerificationToken=.*;)(.*)(vts_usr_lg=.*;)(.*)(vst_usr_lg_token=.*;)(.*)",
            )

        val credentials = loggedCrds["cookies"]?.let { cookies ->
            cookiePattern.find(cookies)?.let { matchResult ->
                val sid = extractValue(matchResult.groupValues[2])
                val rvt = extractValue(matchResult.groupValues[4])
                val vtsUsrLg = extractValue(matchResult.groupValues[6])
                val usrTk = extractValue(matchResult.groupValues[8])

                val csrfAfterLogin = retrieveCookiesAndCsrf(cookies, false)

                VietStockCredential(
                    sid = sid,
                    rvt = rvt,
                    vtsUsrLg = vtsUsrLg,
                    usrTk = usrTk,
                    csrf = csrfAfterLogin?.get("csrf") ?: "",
                )
            }
        } ?: throw IllegalStateException("Failed to retrieve credentials")

        cachedCredentials = credentials
        credentials
    }

    private suspend fun loggedCookies(): Map<String, String> {
        return try {
            val initialData = retrieveCookiesAndCsrf(null, true)
            val cookies = initialData?.get("cookies") ?: return emptyMap()
            val csrf = initialData["csrf"] ?: return emptyMap()

            val response = client.post("https://finance.vietstock.vn/Account/Login") {
                header("Accept", "*/*")
                header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                header("X-Requested-With", "XMLHttpRequest")
                header("Cookie", cookies)
                header("Referer", "https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1")
                header("Referrer-Policy", "strict-origin-when-cross-origin")

                setBody(
                    "__RequestVerificationToken=$csrf" +
                        "&Email=dinhkhoi.le05@gmail.com" +
                        "&Password=536723" +
                        "&responseCaptchaLoginPopup=" +
                        "&g-recaptcha-response=" +
                        "&Remember=false" +
                        "&X-Requested-With=XMLHttpRequest",
                )
            }

            if (!response.status.isSuccess()) {
                logger.error("Login error, status: ${response.status}")
                return emptyMap()
            }

            logger.info("login vietstock res: ${response.bodyAsText()}")

            val cookiesAfterLogin = response.headers
                .getAll("Set-Cookie")
                ?.let { parseCookies(it) }
                ?: ""

            mapOf("cookies" to "$cookies; $cookiesAfterLogin")
        } catch (e: Exception) {
            logger.error("VietStock Login error", e)
            emptyMap()
        }
    }

    private suspend fun retrieveCookiesAndCsrf(initCookies: String?, needCookie: Boolean): Map<String, String>? = try {
        val response = client.get("https://finance.vietstock.vn/doanh-nghiep-a-z/danh-sach-niem-yet?page=1") {
            header(
                "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            )
            header("Sec-Fetch-Dest", "document")
            header("Sec-Fetch-Mode", "navigate")
            header("Sec-Fetch-Site", "same-origin")
            header("Sec-Fetch-User", "?1")
            header("Upgrade-Insecure-Requests", "1")
            header("Referer", "https://www.google.com/")
            header("Referrer-Policy", "origin")

            initCookies?.let {
                header("Cookie", it)
            }
        }

        val responseBody = response.bodyAsText()
        val doc = Jsoup.parse(responseBody)
        val csrf = doc.select("[name=__RequestVerificationToken]")
            .firstOrNull()
            ?.attr("value")
            ?: ""

        val cookies = if (needCookie) {
            response.headers
                .getAll("Set-Cookie")
                ?.let { parseCookies(it) }
                ?: ""
        } else {
            ""
        }

        mapOf(
            "csrf" to csrf,
            "cookies" to cookies,
        )
    } catch (e: Exception) {
        logger.error("retrieveCookiesAndCsrf error", e)
        null
    }

    private fun extractValue(cookie: String): String {
        val startIndex = cookie.indexOf('=') + 1
        return cookie.substring(startIndex, cookie.length - 1)
    }

    private fun parseCookies(rawCookies: List<String>): String = rawCookies.joinToString("; ") {
        it.split(";", limit = 2)[0]
    }
}
