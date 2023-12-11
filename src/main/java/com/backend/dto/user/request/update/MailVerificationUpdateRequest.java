package com.backend.dto.user.request.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailVerificationUpdateRequest {
    @NotEmpty(message = "이메일을 입력 하셔야 합니다!")
    private String email;
    @JsonProperty("auth_code")
    private String authCode;
}
