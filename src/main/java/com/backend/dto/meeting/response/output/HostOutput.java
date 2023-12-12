package com.backend.dto.meeting.response.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HostOutput {
    @JsonProperty("name")
    private String nickname;
    private String profileImage;
    private Boolean isSchoolEmail;

    @QueryProjection
    public HostOutput(String nickname, String profileImage, String schoolEmail) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.isSchoolEmail = schoolEmail != null;
    }
}
