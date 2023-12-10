package com.backend.dto.meeting.request.rating;

import lombok.Builder;

@Builder
public record UserRatingRequest(Long userId, int stars) {
}
