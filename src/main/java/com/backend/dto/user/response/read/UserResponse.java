package com.backend.dto.user.response.read;

import com.backend.entity.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;


public class UserResponse {
    @Builder
    @Getter
    public static class Notification {
        @JsonProperty("is_user_notification")
        private final Boolean isUserNotification;
    }

    @Builder
    @Getter
    public static class MyPage {
        private final String nickname;
        @JsonProperty("profile_image")
        private final String profileImage;
        private final Integer temperature;
        @JsonProperty("my_meetings")
        private final Integer myMeetings;
        @JsonProperty("is_school_verified")
        private final Boolean isSchoolVerified;

        public static MyPage from(User user) {
            return MyPage.builder()
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .temperature(user.getTemperature())
                    .isSchoolVerified(user.getIsSchoolVerified())
                    .myMeetings(user.getMeetings().size())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class Id {
        @JsonProperty("user_id")
        private final Long userId;
    }
}
