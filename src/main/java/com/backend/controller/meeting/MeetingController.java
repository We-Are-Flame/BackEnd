package com.backend.controller.meeting;

import com.backend.annotation.CurrentMember;
import com.backend.dto.meeting.request.create.MeetingCreateRequest;
import com.backend.dto.meeting.response.create.MeetingCreateResponse;
import com.backend.dto.meeting.response.read.MeetingResponse;
import com.backend.entity.user.User;
import com.backend.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<MeetingCreateResponse> createMeeting(@RequestBody MeetingCreateRequest request,
                                                               @CurrentMember User user) {
        Long id = meetingService.createMeeting(request, user);
        MeetingCreateResponse response = MeetingCreateResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<MeetingResponse>> getAllMeetings(Pageable pageable) {
        Page<MeetingResponse> meetings = meetingService.readMeetings(pageable);
        return ResponseEntity.ok(meetings);
    }
}


