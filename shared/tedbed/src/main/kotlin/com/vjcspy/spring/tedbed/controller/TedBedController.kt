package com.vjcspy.spring.tedbed.controller

import java.time.Duration
import kotlin.random.Random
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/tedbed")
class TedBedController {
    @GetMapping("/random-delay")
    fun getRandomDelayResponse(): Mono<String> {
        val randomDelay = Random.nextLong(1000, 5000)
        return Mono
            .just("Response after delay of $randomDelay ms")
            .delayElement(Duration.ofMillis(randomDelay)) // Delay non-blocking
    }
}
