package com.vjcspy.spring.base.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class OkResponse<T>(
    override val success: Boolean = true,
    override val message: String = "OK",
    override val data: T,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val metadata: Map<String, Any> = emptyMap(),
) : BaseResponse<T> {
    companion object {
        fun <T> of(data: T): OkResponse<T> =
            OkResponse(
                data = data,
            )
    }

    fun withMetadata(
        key: String,
        value: Any,
    ): OkResponse<T> =
        copy(
            metadata = metadata + (key to value),
        )
}

/**
 * Extension function to create ResponseEntity from OkResponse
 */
fun <T> OkResponse<T>.toResponseEntity(): ResponseEntity<OkResponse<T>> = ResponseEntity(this, HttpStatus.OK)
