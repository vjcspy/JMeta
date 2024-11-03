package com.vjcspy.spring.application.controller;

import com.vjcspy.spring.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private BaseService baseService;

    @GetMapping
    public ResponseEntity<String> getMessage() {

        return ResponseEntity.ok(baseService.message());
    }
}
