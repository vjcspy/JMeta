package com.vjcspy.spring.base.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class OkResponse<T> implements BaseResponse<T> {
    @Builder.Default
    private final boolean success = true;

    @Builder.Default
    private final String message = "OK";

    private final T data;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private final LocalDateTime timestamp;

    private final Map<String, Object> metadata;

    public static <T> OkResponse<T> of(T data) {
        return OkResponse.<T>builder()
                .data(data)
                .timestamp(LocalDateTime.now())
                .metadata(new HashMap<>())
                .build();
    }

    public OkResponse<T> withMetadata(String key, Object value) {
        Map<String, Object> newMetadata = new HashMap<>(this.metadata);
        newMetadata.put(key, value);
        return OkResponse.<T>builder()
                .success(this.success)
                .message(this.message)
                .data(this.data)
                .timestamp(this.timestamp)
                .metadata(newMetadata)
                .build();
    }

    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }
}