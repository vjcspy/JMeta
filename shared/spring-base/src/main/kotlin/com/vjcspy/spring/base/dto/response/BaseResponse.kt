package com.vjcspy.spring.base.dto.response

interface BaseResponse<T> {
    val success: Boolean
    val message: String
    val data: T
}