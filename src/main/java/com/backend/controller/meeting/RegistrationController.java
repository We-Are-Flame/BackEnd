package com.backend.controller.meeting;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.bases.ResponseMessage;
import com.backend.dto.registration.response.RegistrationApplyResponse;
import com.backend.dto.registration.response.RegistrationResponseList;
import com.backend.entity.user.User;
import com.backend.service.meeting.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RegistrationController {
    private final RegistrationService registrationService;

    @CheckUserNotNull
    @PostMapping("/meetings/{meetingId}/apply")
    public ResponseEntity<RegistrationApplyResponse> applyMeetingRegistration(@PathVariable Long meetingId,
                                                                              @CurrentMember User user) {
        Long id = registrationService.applyMeeting(meetingId, user);
        RegistrationApplyResponse response = RegistrationApplyResponse.success(id,
                ResponseMessage.REGISTRATION_CREATION_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PostMapping("/meetings/{meetingId}/cancel")
    public ResponseEntity<RegistrationApplyResponse> cancelMeetingRegistration(@PathVariable Long meetingId,
                                                                               @CurrentMember User user) {
        Long id = registrationService.cancelMeeting(meetingId, user);
        RegistrationApplyResponse response = RegistrationApplyResponse.success(id,
                ResponseMessage.REGISTRATION_CANCEL_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @GetMapping("/meetings/{meetingId}/registrations")
    public ResponseEntity<RegistrationResponseList> getMeetingRegistration(@PathVariable Long meetingId,
                                                                           @CurrentMember User user) {
        RegistrationResponseList response = registrationService.getRegistration(meetingId, user);
        return ResponseEntity.ok(response);
    }
}
