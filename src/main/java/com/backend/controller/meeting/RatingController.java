package com.backend.controller.meeting;

import com.backend.dto.common.SuccessResponse;
import com.backend.dto.meeting.request.rating.UserRatingRequestList;
import com.backend.service.meeting.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/api/meetings/{meetingId}/ratings")
    public ResponseEntity<SuccessResponse> rateMeetingParticipants(
            @PathVariable Long meetingId,
            @RequestBody UserRatingRequestList requestList) {
        ratingService.ratingMeetingParticipants(requestList);
        return ResponseEntity.ok().build();
    }
}
