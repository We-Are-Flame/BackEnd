package com.backend.dto.user.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class MailResponse {

    @JsonProperty("is_verified")
    private final Boolean isVerified;
}
