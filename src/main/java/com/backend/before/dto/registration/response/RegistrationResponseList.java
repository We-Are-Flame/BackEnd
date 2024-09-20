package com.backend.before.dto.registration.response;

import java.util.List;

public record RegistrationResponseList(int count, List<RegistrationResponse> content) {
}
