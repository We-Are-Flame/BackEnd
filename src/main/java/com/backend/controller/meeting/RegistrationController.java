package com.backend.controller.meeting;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.bases.ResponseMessage;
import com.backend.dto.registration.response.RegistrationResponse;
import com.backend.entity.user.User;
import com.backend.service.meeting.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meetings")
public class RegistrationController {
    private final RegistrationService registrationService;

    @CheckUserNotNull
    @PostMapping("/{meetingId}/apply")
    public ResponseEntity<RegistrationResponse> applyMeeting(@PathVariable Long meetingId, @CurrentMember User user) {
        Long id = registrationService.applyMeeting(meetingId, user);
        RegistrationResponse response = RegistrationResponse.success(id, ResponseMessage.REGISTRATION_CREATION_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PostMapping("/{meetingId}/cancel")
    public ResponseEntity<RegistrationResponse> cancelMeeting(@PathVariable Long meetingId, @CurrentMember User user) {
        Long id = registrationService.cancelMeeting(meetingId, user);
        RegistrationResponse response = RegistrationResponse.success(id,ResponseMessage.REGISTRATION_CANCEL_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
