package com.backend.registration.controller;

import com.backend.before.annotation.CheckUserNotNull;
import com.backend.before.annotation.CurrentMember;
import com.backend.before.dto.common.ResponseMessage;
import com.backend.before.dto.common.SuccessResponse;
import com.backend.before.dto.registration.request.BulkApplyRequest;
import com.backend.before.dto.registration.response.AcceptResponse;
import com.backend.before.dto.registration.response.RegistrationResponseList;
import com.backend.before.dto.registration.response.RejectResponse;
import com.backend.before.entity.user.User;
import com.backend.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
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
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @GetMapping("/{meetingId}/registrations")
    public ResponseEntity<RegistrationResponseList> getApply(@PathVariable Long meetingId, @CurrentMember User user) {
        RegistrationResponseList response = registrationService.getRegistrations(meetingId);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @PostMapping("/{meetingId}/accept")
    public ResponseEntity<AcceptResponse> acceptApply(@PathVariable Long meetingId,
                                                      @RequestBody BulkApplyRequest request,
                                                      @CurrentMember User user) {
        AcceptResponse response = registrationService.acceptApply(meetingId, request.getRegistrationIds());
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @PostMapping("/{meetingId}/reject")
    public ResponseEntity<RejectResponse> rejectApply(@PathVariable Long meetingId,
                                                      @RequestBody BulkApplyRequest request,
                                                      @CurrentMember User user) {
        RejectResponse response = registrationService.rejectApply(request.getRegistrationIds());
        return ResponseEntity.ok(response);
    }
}
