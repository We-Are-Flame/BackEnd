package com.backend.controller.meeting;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.common.ResponseMessage;
import com.backend.dto.common.SuccessResponse;
import com.backend.dto.registration.request.BulkApplyRequest;
import com.backend.dto.registration.response.RegistrationResponseList;
import com.backend.entity.user.User;
import com.backend.service.meeting.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @GetMapping("/{meetingId}/registrations")
    public ResponseEntity<RegistrationResponseList> getApply(@PathVariable Long meetingId, @CurrentMember User user) {
        RegistrationResponseList response = registrationService.getRegistration(meetingId);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @PostMapping("/{meetingId}/registrations/{registrationId}/accept")
    public ResponseEntity<SuccessResponse> acceptApply(@PathVariable Long meetingId, @PathVariable Long registrationId,
                                                       @CurrentMember User user) {
        Long id = registrationService.acceptApply(registrationId);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.APPLY_ACCEPT);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @PostMapping("/{meetingId}/registrations/{registrationId}/reject")
    public ResponseEntity<SuccessResponse> rejectApply(@PathVariable Long meetingId, @PathVariable Long registrationId,
                                                       @CurrentMember User user) {
        Long id = registrationService.rejectApply(registrationId);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.APPLY_REJECT);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @PostMapping("/{meetingId}/registrations/bulk-accept")
    public ResponseEntity<SuccessResponse> acceptApplyBulk(@PathVariable Long meetingId,
                                                           @RequestBody BulkApplyRequest request,
                                                           @CurrentMember User user) {
        Long id = registrationService.acceptBulkApply(request.getRegistrationIds());
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.APPLY_ACCEPT);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @PostMapping("/{meetingId}/registrations/bulk-reject")
    public ResponseEntity<SuccessResponse> rejectApplyBulk(@PathVariable Long meetingId,
                                                           @RequestBody BulkApplyRequest request,
                                                           @CurrentMember User user) {
        Long id = registrationService.rejectBulkApply(request.getRegistrationIds());
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.APPLY_REJECT);
        return ResponseEntity.ok(response);
    }
}
