package com.backend.dto.chat.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoomNotificationResponse {

    @JsonProperty("is_notification")
    Boolean isNotification;
}
