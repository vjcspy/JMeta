// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.application.controller

import com.vjcspy.spring.base.config.Env
import com.vjcspy.spring.base.exception.BusinessException
import com.vjcspy.spring.base.exception.ErrorCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(
    private val env: Env,
) {
    @GetMapping
    fun getMessage(): ResponseEntity<String> = ResponseEntity.ok("OK")

    @GetMapping("/error")
    fun testError(): ResponseEntity<String> = throw BusinessException(ErrorCode.SYSTEM_ERROR)

    @GetMapping("/env")
    fun getEnv(): String = "appName: ${env.get("APP_NAME")}, version: ${env.get("APP_VERSION")}"
}
