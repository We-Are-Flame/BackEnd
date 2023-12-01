package com.backend.dto.common;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessResponse {
    private final Long id;
    private final ResponseStatus status;
    @Builder.Default
    private final LocalDateTime timeStamp = LocalDateTime.now();
    private final String message;

    public static SuccessResponse create(Long id, ResponseMessage responseMessage) {
        return SuccessResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(responseMessage.getMessage())
                .build();
    }
}
