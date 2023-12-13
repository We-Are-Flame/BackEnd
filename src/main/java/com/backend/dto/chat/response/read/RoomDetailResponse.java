package com.backend.dto.chat.response.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;


public class RoomDetailResponse {
    @Builder
    @Getter
    public static class Notification {
        @JsonProperty("is_notification")
        Boolean isNotification;
    }

    @Builder
    @Getter
    public static class Title {
        String title;
    }

    @Builder
    @Getter
    public static class Thumbnail {
        String thumbnail;
    }

    @Builder
    @Getter
    public static class Host {
        @JsonProperty("is_host")
        Boolean isHost;
    }

}
