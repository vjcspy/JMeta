package com.vjcspy.spring.base.dto.response;

public interface BaseResponse<T> {
    boolean isSuccess();
    String getMessage();
    T getData();
}