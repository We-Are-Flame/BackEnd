package com.backend.before.dto.user.response.update;

import com.backend.before.dto.common.BaseResponse;
import com.backend.before.dto.common.ResponseMessage;
import com.backend.before.dto.common.ResponseStatus;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
public class UserUpdateResponse {

    @SuperBuilder
    public static class Nickname extends BaseResponse {
        private final Long id;

        public static Nickname success(Long id) {
            return Nickname.builder()
                    .id(id)
                    .status(ResponseStatus.SUCCESS)
                    .message(ResponseMessage.NICKNAME_UPDATE_SUCCESS.getMessage())
                    .build();
        }
    }

    @SuperBuilder
    public static class ProfileImage extends BaseResponse {
        private final Long id;

        public static ProfileImage success(Long id) {
            return ProfileImage.builder()
                    .id(id)
                    .status(ResponseStatus.SUCCESS)
                    .message(ResponseMessage.PROFILE_IMAGE_UPDATE_SUCCESS.getMessage())
                    .build();
        }
    }

    @SuperBuilder
    public static class Notification extends BaseResponse {
        private final Long id;

        public static Notification success(Long id) {
            return Notification.builder()
                    .id(id)
                    .status(ResponseStatus.SUCCESS)
                    .message(ResponseMessage.NOTIFICATION_UPDATE_SUCCESS.getMessage())
                    .build();
        }
    }

}
