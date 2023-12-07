package com.backend.util.mapper.registration;

import com.backend.dto.registration.response.AcceptResponse;
import com.backend.dto.registration.response.RegistrationResponse;
import com.backend.dto.registration.response.RejectResponse;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.user.User;
import java.util.List;

public class RegistrationResponseMapper {
    public static AcceptResponse buildAccept(String roomId, List<Long> registrationIds) {
        return AcceptResponse.builder()
                .roomId(roomId)
                .registrationIds(registrationIds)
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
                .build();
    }
}
