package com.backend.dto.user.request.create;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailVerificationRequest {
    @NotEmpty(message = "이메일을 입력 하셔야 합니다!")
    private String email;
}
