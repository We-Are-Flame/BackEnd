package com.backend.controller.meeting;

import com.backend.dto.meeting.request.MeetingCreateRequest;
import com.backend.dto.meeting.response.MeetingCreateResponse;
import com.backend.entity.user.User;
import com.backend.repository.user.UserRepository;
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
    private final UserRepository userRepository;

    @PostMapping("/meetings")
    public ResponseEntity<MeetingCreateResponse> createMeeting(@RequestBody MeetingCreateRequest request) {
        User user = mockingUser();

        Long id = meetingService.createMeeting(request);
        MeetingCreateResponse response = MeetingCreateResponse.success(id);
        return ResponseEntity.ok(response);
    }

    private User mockingUser() {
        return userRepository.findById(1L).get();
    }
}


