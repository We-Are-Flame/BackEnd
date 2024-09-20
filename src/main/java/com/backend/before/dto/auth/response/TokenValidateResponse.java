package com.backend.before.dto.auth.response;

import com.backend.before.dto.common.BaseResponse;
import com.backend.before.dto.common.ResponseMessage;
import com.backend.before.dto.common.ResponseStatus;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TokenValidateResponse extends BaseResponse {
    private Long id;

    public static TokenValidateResponse success(Long id) {
        return TokenValidateResponse.builder()
                .id(id)
                .status(ResponseStatus.SUCCESS)
                .message(ResponseMessage.TOKEN_VALIDATE_SUCCESS.getMessage())
                .build();
    }
}
