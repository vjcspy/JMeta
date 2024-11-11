/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.controller;

import com.vjcspy.spring.base.dto.response.OkResponse;
import com.vjcspy.spring.packages.stocksync.service.CorService;
import com.vjcspy.spring.packages.stocksync.service.VietStockCredentialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock-sync")
public class StockSyncController {
    private final VietStockCredentialService vietStockCredentialService;

    private final CorService corService;

    public StockSyncController(VietStockCredentialService vietStockCredentialService, CorService corService) {
        this.vietStockCredentialService = vietStockCredentialService;
        this.corService = corService;
    }

    @GetMapping("/vs-cred")
    public ResponseEntity<OkResponse<?>> getAllCorEntities() {
        var data = vietStockCredentialService.retrieveCredentials();
        return ResponseEntity.ok(OkResponse.of(data));
    }

    @GetMapping("/get-cors-from-viet-stock")
    public ResponseEntity<OkResponse<?>> getAllCors(@RequestParam(defaultValue = "0") int page) {
        var data = corService.getCorporateData(page);
        return ResponseEntity.ok(OkResponse.of(data));
    }
}
