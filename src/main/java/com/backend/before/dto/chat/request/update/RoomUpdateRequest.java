package com.backend.before.dto.chat.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class RoomUpdateRequest {

    @Getter
    @Setter
    public static class Notification {
        @NotNull
        private Boolean isNotification;
    }

    @Getter
    @Setter
    public static class Title {
        String title;
    }

}
