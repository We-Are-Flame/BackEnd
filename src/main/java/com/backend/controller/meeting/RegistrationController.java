package com.backend.controller.meeting;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
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
    public ResponseEntity<RegistrationResponse> createMeeting(@PathVariable Long meetingId, @CurrentMember User user) {
        Long id = registrationService.applyMeeting(meetingId, user);
        RegistrationResponse response = RegistrationResponse.success(id);
        return ResponseEntity.ok(response);
    }
}
