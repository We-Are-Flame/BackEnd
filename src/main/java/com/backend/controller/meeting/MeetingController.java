package com.backend.controller.meeting;

import com.backend.annotation.CheckUserNotNull;
import com.backend.annotation.CurrentMember;
import com.backend.dto.common.ResponseMessage;
import com.backend.dto.common.SuccessResponse;
import com.backend.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.dto.meeting.response.MeetingDetailResponse;
import com.backend.dto.meeting.response.MeetingResponse;
import com.backend.dto.meeting.response.MyMeetingResponseList;
import com.backend.entity.user.User;
import com.backend.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    @CheckUserNotNull
    public ResponseEntity<SuccessResponse> createMeeting(@RequestBody MeetingCreateRequest request,
                                                         @CurrentMember User user) {
        Long id = meetingService.createMeeting(request, user);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.MEETING_CREATION);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<MeetingResponse>> getAllMeetings(
            @RequestParam int index,
            @RequestParam int size,
            @RequestParam String sort) {
        Page<MeetingResponse> meetings = meetingService.readMeetings(index, size, sort);
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingDetailResponse> getMeeting(@PathVariable Long meetingId, @CurrentMember User user) {
        MeetingDetailResponse meeting = meetingService.readOneMeeting(meetingId, user);
        return ResponseEntity.ok(meeting);
    }

    @CheckUserNotNull
    @PreAuthorize("hasAuthority('ROLE_HOST')")
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<SuccessResponse> deleteMeeting(@PathVariable Long meetingId, @CurrentMember User user) {
        Long id = meetingService.deleteMeeting(meetingId);
        SuccessResponse response = SuccessResponse.create(id, ResponseMessage.DELETE_MEETING);
        return ResponseEntity.ok(response);
    }

    @CheckUserNotNull
    @GetMapping("/my")
    public ResponseEntity<MyMeetingResponseList> getMyMeetings(@CurrentMember User user) {
        MyMeetingResponseList meetings = meetingService.readMyMeetings(user);
        return ResponseEntity.ok(meetings);
    }

    @CheckUserNotNull
    @GetMapping("/participated")
    public ResponseEntity<MyMeetingResponseList> getParticipatedMeetings(@CurrentMember User user) {
        MyMeetingResponseList meetings = meetingService.readParticipatedMeetings(user);
        return ResponseEntity.ok(meetings);
    }
}
