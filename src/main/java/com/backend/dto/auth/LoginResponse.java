package com.backend.dto.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {

    private Long id;
    private String accessToken;

}