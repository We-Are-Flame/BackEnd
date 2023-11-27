package com.backend.dto.user.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;


public class UserResponse {
    @Builder
    @Getter
    public static class Notification{
        @JsonProperty("is_user_notification")
        private final Boolean isUserNotification;
    }
}
