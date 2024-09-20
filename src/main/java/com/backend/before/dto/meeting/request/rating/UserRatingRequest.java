package com.backend.before.dto.meeting.request.rating;

import lombok.Builder;

@Builder
public record UserRatingRequest(Long userId, int stars) {
}
