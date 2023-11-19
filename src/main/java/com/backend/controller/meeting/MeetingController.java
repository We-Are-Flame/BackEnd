package com.backend.controller.meeting;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.dto.meeting.response.MeetingCreateResponse;
import com.backend.entity.meeting.Meeting;
import com.backend.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping("/meetings")
    public ResponseEntity<MeetingCreateResponse> createMeeting(@RequestBody MeetingCreateRequest request) {
        Meeting meeting = meetingService.createMeeting(request);
        MeetingCreateResponse response = MeetingCreateResponse.success(meeting.getId());
        return ResponseEntity.ok(response);
    }
}


