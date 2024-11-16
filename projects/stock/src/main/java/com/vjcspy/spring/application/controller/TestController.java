/* (C) 2024 */
package com.vjcspy.spring.application.controller;

import com.vjcspy.spring.base.config.Env;
import com.vjcspy.spring.base.exception.BusinessException;
import com.vjcspy.spring.base.exception.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private Env env;

    @GetMapping
    public ResponseEntity<String> getMessage() {
        log.info("getMessage");
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/error")
    public ResponseEntity<String> testError() {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }

    @GetMapping("/env")
    public String getEnv() {
        return "appName: " + env.get("APP_NAME") + ", version: " + env.get("APP_VERSION");
    }
}
