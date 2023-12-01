package com.backend.dto.user.request.update;

import com.backend.dto.user.request.update.input.ImageInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public class UserUpdateRequest {
    @Getter
    @Setter
    public static class Nickname {
        @NotEmpty(message = "내용이 있어야 합니다!")
        private String nickname;
    }

    @Getter
    @Setter
    public static class ProfileImage {
        @JsonProperty("image")
        private ImageInput imageInput;
    }

    @Getter
    @Setter
    public static class Notification {
        @NotNull
        private Boolean isNotification;
    }


}
