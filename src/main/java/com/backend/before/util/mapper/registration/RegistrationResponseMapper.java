package com.backend.before.util.mapper.registration;

import com.backend.before.dto.registration.response.AcceptResponse;
import com.backend.before.dto.registration.response.AcceptResponse.UserInfo;
import com.backend.before.dto.registration.response.RegistrationResponse;
import com.backend.before.dto.registration.response.RejectResponse;
import com.backend.registration.entity.MeetingRegistration;
import com.backend.before.entity.user.User;
import java.util.List;

public class RegistrationResponseMapper {
    public static AcceptResponse buildAccept(String roomId, List<Long> registrationIds, List<User> users) {
        List<UserInfo> userInfos = users.stream()
                .map(user -> new UserInfo(user.getId(), user.getNickname()))
                .toList();

        return AcceptResponse.builder()
                .roomId(roomId)
                .registrationIds(registrationIds)
                .userInfos(userInfos)
                .build();
    }

    public static RejectResponse buildReject(List<Long> registrationIds) {
        return RejectResponse.builder()
                .registrationIds(registrationIds)
                .build();
    }

    public static RegistrationResponse buildRegistrationResponse(MeetingRegistration registration) {
        User registeredUser = registration.getUser();
        return RegistrationResponse.builder()
                .id(registration.getId())
                .nickname(registeredUser.getNickname())
                .profileImage(registeredUser.getProfileImage())
                .temperature(registeredUser.getTemperature())
                .participateStatus(registration.getStatus())
                .isSchoolEmail(registeredUser.getIsSchoolVerified())
                .build();
    }
}
