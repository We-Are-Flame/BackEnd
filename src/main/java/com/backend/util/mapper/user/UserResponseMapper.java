package com.backend.util.mapper.user;

import com.backend.dto.user.response.read.MailResponse;
import com.backend.dto.user.response.read.UserResponse;
import com.backend.entity.user.User;

public class UserResponseMapper {

    public static UserResponse.Notification buildUserNotification(User user) {
        return UserResponse.Notification.builder()
                .isUserNotification(user.getSetting().getIsUserNotification())
                .build();
    }

    public static MailResponse buildMailResponse(User user) {
        boolean isVerified = user.getIsSchoolVerified();

        return MailResponse.builder()
                .isVerified(isVerified)
                .build();
    }


    public static UserResponse.Id buildUserIdResponse(Long userId) {
        return UserResponse.Id.builder()
                .userId(userId)
                .build();
    }
}
