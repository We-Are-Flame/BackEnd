package com.backend.dto.registration.response;

import com.backend.dto.bases.BaseResponse;
import com.backend.dto.bases.ResponseMessage;
import com.backend.dto.bases.ResponseStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RegistrationResponse extends BaseResponse {
    private final Long id;

    public static RegistrationResponse success(Long id, ResponseMessage message) {
        return RegistrationResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(message.getMessage())
                .build();
    }
}
