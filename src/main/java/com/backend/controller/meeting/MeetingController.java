package com.backend.controller.meeting;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.dto.meeting.response.MeetingCreateResponse;
import com.backend.entity.user.User;
import com.backend.service.meeting.MeetingService;
import com.backend.util.mock.UserMocking;
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
    private final UserMocking userMocking;

    @PostMapping("/meetings")
    public ResponseEntity<MeetingCreateResponse> createMeeting(@RequestBody MeetingCreateRequest request) {
        ///TODO [HJ] 로그인 구현되는대로 실제 로직으로 변경
        User user = userMocking.findOrMockUser();

        Long id = meetingService.createMeeting(request, user);
        MeetingCreateResponse response = MeetingCreateResponse.success(id);
        return ResponseEntity.ok(response);
    }
}


