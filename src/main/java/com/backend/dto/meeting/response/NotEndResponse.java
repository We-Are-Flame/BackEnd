package com.backend.dto.meeting.response;

import com.querydsl.core.annotations.QueryProjection;

public record NotEndResponse(Long id, String title) {
    @QueryProjection
    public NotEndResponse {
    }
}
