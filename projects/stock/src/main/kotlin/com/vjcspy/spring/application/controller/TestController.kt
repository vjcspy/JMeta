// (mr.vjcspy@gmail.com) 2024
package com.vjcspy.spring.application.controller

import com.vjcspy.spring.base.exception.BusinessException
import com.vjcspy.spring.base.exception.ErrorCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {
    @GetMapping
    fun getMessage(): ResponseEntity<String> = ResponseEntity.ok("OK")

    @GetMapping("/error")
    fun testError(): ResponseEntity<String> = throw BusinessException(ErrorCode.SYSTEM_ERROR)
}
