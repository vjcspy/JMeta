package com.vjcspy.spring.base.dto.response


import com.fasterxml.jackson.annotation.JsonFormat
import com.vjcspy.spring.base.exception.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class ErrorResponse(
    override val success: Boolean = false,
    val code: String,
    override val message: String,
    val httpStatus: Int,
    val httpStatusText: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val path: String,
    override val data: Any? = null,
    val errors: List<ValidationError> = emptyList(),
    val stackTrace: List<String> = emptyList()
) : BaseResponse<Any?> {

    data class ValidationError(
        val field: String,
        val code: String,
        val message: String,
        val rejectedValue: Any?
    )

    companion object {
        fun of(errorCode: ErrorCode, path: String) = ErrorResponse(
            code = errorCode.code,
            message = errorCode.message,
            httpStatus = errorCode.httpStatus.value(),
            httpStatusText = errorCode.httpStatus.reasonPhrase,
            path = path
        )

        fun of(code: String, message: String, status: HttpStatus, path: String) = ErrorResponse(
            code = code,
            message = message,
            httpStatus = status.value(),
            httpStatusText = status.reasonPhrase,
            path = path
        )

        fun of(
            code: String,
            message: String,
            status: HttpStatus,
            path: String,
            errors: List<ValidationError>
        ) = ErrorResponse(
            code = code,
            message = message,
            httpStatus = status.value(),
            httpStatusText = status.reasonPhrase,
            path = path,
            errors = errors
        )
    }

    fun withStackTrace(ex: Throwable): ErrorResponse = copy(
        stackTrace = ex.stackTrace.map { it.toString() }
    )

    fun addValidationError(
        field: String,
        code: String,
        message: String,
        rejectedValue: Any?
    ): ErrorResponse = copy(
        errors = errors + ValidationError(field, code, message, rejectedValue)
    )
}

/**
 * Extension function to create ResponseEntity from ErrorResponse
 */
fun ErrorResponse.toResponseEntity(): ResponseEntity<ErrorResponse> =
    ResponseEntity(this, HttpStatus.valueOf(httpStatus))