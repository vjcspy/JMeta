// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.controller

import com.vjcspy.rxevent.RxEventManager
import com.vjcspy.spring.base.dto.response.OkResponse
import com.vjcspy.spring.base.dto.response.toResponseEntity
import com.vjcspy.spring.packages.stocksync.dto.vietstock.CorporateData
import com.vjcspy.spring.packages.stocksync.dto.vietstock.VietStockCredential
import com.vjcspy.spring.packages.stocksync.rxevent.cor.CorAction
import com.vjcspy.spring.packages.stocksync.service.CorService
import com.vjcspy.spring.packages.stocksync.service.VietStockCredentialService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stock-sync")
class StockSyncController(
    private val vietStockCredentialService: VietStockCredentialService,
    private val corService: CorService,
) {
    @GetMapping("/vs-cred")
    fun getAllCorEntities(): ResponseEntity<OkResponse<VietStockCredential>> {
        val data = vietStockCredentialService.retrieveCredentials()
        return OkResponse.of(data).toResponseEntity()
    }

    @GetMapping("/get-cors-from-viet-stock")
    fun getAllCors(
        @RequestParam(defaultValue = "0") page: Int,
    ): ResponseEntity<OkResponse<List<CorporateData>>> {
        val data = corService.getCorporateData(page).blockingGet()
        return OkResponse.of(data).toResponseEntity()
    }

    @GetMapping("/test-sync-cor")
    fun testSyncCor(): ResponseEntity<OkResponse<Nothing?>> {
        RxEventManager.dispatch(
            CorAction.COR_LOAD_NEXT_PAGE_ACTION(
                mapOf("currentPage" to 1),
            ),
        )
        return OkResponse.of(null).toResponseEntity()
    }
}
