package com.backend.dto.meeting.response.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HostOutput {
    @JsonProperty("name")
    private String nickname;
    private String profileImage;

    @QueryProjection
    public HostOutput(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
