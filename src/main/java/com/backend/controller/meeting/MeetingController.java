package com.backend.controller.meeting;

import com.backend.annotation.CurrentMember;
import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.dto.meeting.response.MeetingCreateResponse;
import com.backend.dto.meeting.response.MeetingReadResponse;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.user.User;
import com.backend.service.meeting.MeetingService;
import com.backend.util.mock.UserMocking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final UserMocking userMocking;

    @PostMapping
    public ResponseEntity<MeetingCreateResponse> createMeeting(@RequestBody MeetingCreateRequest request, @CurrentMember User user) {

        Long id = meetingService.createMeeting(request, user);
        MeetingCreateResponse response = MeetingCreateResponse.success(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meetings")
    public ResponseEntity<Page<MeetingReadResponse>> getMeetings(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {

        Page<MeetingReadResponse>meetings = meetingService.readMeetings(page, size, sort);
        return ResponseEntity.ok(meetings);
    }
}


