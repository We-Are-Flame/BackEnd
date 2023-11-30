package com.backend.dto.registration.response;

import com.backend.dto.bases.BaseResponse;
import com.backend.dto.bases.ResponseMessage;
import com.backend.dto.bases.ResponseStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RegistrationApplyResponse extends BaseResponse {
    private final Long id;

    public static RegistrationApplyResponse success(Long id, ResponseMessage message) {
        return RegistrationApplyResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(message.getMessage())
                .build();
    }
}
