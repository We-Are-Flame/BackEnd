package com.backend.before.dto.meeting.response;

import com.backend.before.dto.meeting.response.output.DetailInfoOutput;
import com.backend.before.dto.meeting.response.output.DetailTimeOutput;
import com.backend.before.dto.meeting.response.output.HostOutput;
import com.backend.before.dto.meeting.response.output.LocationOutput;
import com.backend.before.dto.meeting.response.output.MeetingImageOutput;
import com.backend.before.dto.meeting.response.output.StatusOutput;
import com.backend.registration.entity.RegistrationRole;
import com.backend.registration.entity.RegistrationStatus;
import com.backend.before.util.etc.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;

import java.util.List;
import java.util.Optional;

import lombok.Getter;

@Getter
public class MeetingDetailResponse {
    private final Long id;
    private final String category;
    private final List<String> hashtags;
    @JsonProperty("info")
    private final DetailInfoOutput detailInfoOutput;

    @JsonProperty("image")
    private final MeetingImageOutput imageOutput;

    @JsonProperty("location")
    private final LocationOutput locationOutput;

    @JsonProperty("time")
    private final DetailTimeOutput timeOutput;

    @JsonProperty("host")
    private final HostOutput hostOutput;
    @JsonIgnore
    private final RegistrationStatus registrationStatus;
    @JsonIgnore
    private final RegistrationRole registrationRole;
    @JsonProperty("status")
    private StatusOutput statusOutput;

    @QueryProjection
    public MeetingDetailResponse(long id, String category, String hashtag, DetailInfoOutput detailInfoOutput,
                                 MeetingImageOutput imageOutput, LocationOutput locationOutput,
                                 DetailTimeOutput timeOutput, HostOutput hostOutput,
                                 RegistrationStatus registrationStatus, RegistrationRole registrationRole) {
        this.id = id;
        this.category = category;
        this.hashtags = StringUtil.split(hashtag);
        this.detailInfoOutput = detailInfoOutput;
        this.imageOutput = imageOutput;
        this.locationOutput = locationOutput;
        this.timeOutput = timeOutput;
        this.hostOutput = hostOutput;
        this.registrationStatus = Optional.ofNullable(registrationStatus).orElse(RegistrationStatus.NONE);
        this.registrationRole = Optional.ofNullable(registrationRole).orElse(RegistrationRole.MEMBER);
    }

    public MeetingDetailResponse updateStatus() {
        this.statusOutput = StatusOutput.builder()
                .isOwner(this.registrationRole.isOwner(this.registrationRole))
                .participateStatus(this.registrationStatus)
                .isExpire(this.timeOutput.isExpired())
                .isFull(this.detailInfoOutput.isFull())
                .build();
        return this;
    }
}
