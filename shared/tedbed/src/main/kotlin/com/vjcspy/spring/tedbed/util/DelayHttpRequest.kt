package com.vjcspy.spring.tedbed.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class DelayHttpRequest(
    @Value("\${server.port:8080}")
    port: String
) {
    companion object {
        private lateinit var webClient: WebClient

        fun initialize(port: String) {
            webClient = WebClient.builder()
                .baseUrl("http://localhost:$port")
                .build()
        }

        fun makeDelayHttpRequest(): Mono<String> =
            webClient
                .get()
                .uri("/tedbed/random-delay")
                .retrieve()
                .onStatus({ status -> !status.is2xxSuccessful }) { response ->
                    Mono.error(RuntimeException("HTTP error: ${response.statusCode()}"))
                }
                .bodyToMono(String::class.java)
    }

    init {
        initialize(port)
    }
}
