// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.packages.stocksync.controller

import com.vjcspy.spring.base.dto.response.OkResponse
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
    fun getAllCorEntities(): ResponseEntity<OkResponse<*>> {
        val data = vietStockCredentialService.retrieveCredentials()
        return ResponseEntity.ok(OkResponse.of(data))
    }

    @GetMapping("/get-cors-from-viet-stock")
    fun getAllCors(
        @RequestParam(defaultValue = "0") page: Int,
    ): ResponseEntity<OkResponse<*>> {
        val data = corService.getCorporateData(page)
        return ResponseEntity.ok(OkResponse.of(data))
    }
}
