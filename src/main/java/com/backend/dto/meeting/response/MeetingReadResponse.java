package com.backend.dto.meeting.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingReadResponse {
    private Long id;
    private String title;

    public MeetingReadResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
