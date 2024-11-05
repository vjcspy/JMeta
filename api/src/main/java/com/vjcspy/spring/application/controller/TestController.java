package com.vjcspy.spring.application.controller;

import com.vjcspy.spring.base.BaseService;
import com.vjcspy.spring.base.exception.BusinessException;
import com.vjcspy.spring.base.exception.constant.ErrorCode;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private BaseService baseService;

    @GetMapping
    public ResponseEntity<String> getMessage() {
        log.info("getMessage");
        return ResponseEntity.ok(baseService.message());
    }

    @GetMapping("/error")
    public ResponseEntity<String> testError() {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    }
}
