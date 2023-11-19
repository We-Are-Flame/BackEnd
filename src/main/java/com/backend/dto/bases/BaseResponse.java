package com.backend.dto.bases;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BaseResponse {
    private final ResponseStatus status;
    @Builder.Default
    private final LocalDateTime timeStamp = LocalDateTime.now();
    private final String message;
}


