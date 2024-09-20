package com.backend.before.dto.common;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SuccessResponse {
    private final Long id;
    @Builder.Default
    private final ResponseStatus status = ResponseStatus.SUCCESS;
    @Builder.Default
    private final LocalDateTime timeStamp = LocalDateTime.now();
    private final String message;

    public static SuccessResponse create(Long id, ResponseMessage responseMessage) {
        return SuccessResponse.builder()
                .id(id)
                .message(responseMessage.getMessage())
                .build();
    }
}
