package com.backend.controller.meeting;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.common.ResponseMessage;
import com.backend.dto.common.SuccessResponse;
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
@RequestMapping("/api/meetings")
public class RegistrationController {
    private final RegistrationService registrationService;

    @CheckUserNotNull
    @PostMapping("/{meetingId}/apply")
    public ResponseEntity<SuccessResponse> applyMeeting(@PathVariable Long meetingId, @CurrentMember User user) {
        Long id = registrationService.applyMeeting(meetingId, user);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.APPLY_MEETING);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PostMapping("/{meetingId}/cancel")
    public ResponseEntity<SuccessResponse> cancelApply(@PathVariable Long meetingId, @CurrentMember User user) {
        Long id = registrationService.cancelMeeting(meetingId, user);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.CANCEL_MEETING);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @GetMapping("/{meetingId}/registrations")
    public ResponseEntity<RegistrationResponseList> getApply(@PathVariable Long meetingId, @CurrentMember User user) {
        RegistrationResponseList response = registrationService.getRegistration(meetingId, user);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PostMapping("/{meetingId}/registrations/{registrationId}/accept")
    public ResponseEntity<SuccessResponse> acceptApply(@PathVariable Long registrationId, @PathVariable Long meetingId,
                                                       @CurrentMember User user) {
        Long id = registrationService.acceptApply(registrationId, meetingId, user);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.APPLY_ACCEPT);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PostMapping("/{meetingId}/registrations/{registrationId}/reject")
    public ResponseEntity<SuccessResponse> rejectApply(@PathVariable Long registrationId, @PathVariable Long meetingId,
                                                       @CurrentMember User user) {
        Long id = registrationService.rejectApply(registrationId, meetingId, user);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.APPLY_REJECT);
        return ResponseEntity.ok(response);
    }
}
