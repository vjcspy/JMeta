package com.vjcspy.spring.packages.example.controller

import com.vjcspy.spring.packages.example.queue.RabbitExamplePublisher
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class RabbitExampleController(
    private val rabbitExamplePublisher: RabbitExamplePublisher,
) {
    @GetMapping("/rabbit")
    fun sendMessage() {
        repeat(10) {
            // Lặp qua n lần
            rabbitExamplePublisher.sendMessage("$it")
        }
    }
}
