/* (C) 2024 */
package com.vjcspy.spring.packages.stocksync.controller;

import com.vjcspy.spring.base.dto.response.OkResponse;
import com.vjcspy.spring.packages.stocksync.service.VietStockCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock-sync")
public class StockSyncController {
    @Autowired
    private VietStockCredentialService vietStockCredentialService;

    @GetMapping("/vs-cred")
    public ResponseEntity<OkResponse<?>> getAllCorEntities() {
        var data = vietStockCredentialService.retrieveCredentials();
        return ResponseEntity.ok(OkResponse.of(data));
    }
}
