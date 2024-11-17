package com.vjcspy.spring.base.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController {

    private val log = KotlinLogging.logger {}

    @GetMapping
    fun checkHealth(): ResponseEntity<String> {
        log.info { "Health check requested" }
        return ResponseEntity.ok("ok")
    }
}