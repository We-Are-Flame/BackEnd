package com.backend.dto.meeting.response.read;

import com.backend.dto.meeting.response.read.output.DetailInfoOutput;
import com.backend.dto.meeting.response.read.output.DetailTimeOutput;
import com.backend.dto.meeting.response.read.output.HostOutput;
import com.backend.dto.meeting.response.read.output.LocationOutput;
import com.backend.dto.meeting.response.read.output.MeetingImageOutput;
import com.backend.dto.meeting.response.read.output.StatusOutput;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingDetailResponse {
    private final Long id;
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

    @JsonProperty("status")
    private final StatusOutput statusOutput;
}
