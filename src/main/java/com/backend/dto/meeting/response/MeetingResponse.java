package com.backend.dto.meeting.response;

import com.backend.dto.meeting.response.output.HostOutput;
import com.backend.dto.meeting.response.output.InfoOutput;
import com.backend.dto.meeting.response.output.LocationOutput;
import com.backend.dto.meeting.response.output.TimeOutput;
import com.backend.util.etc.StringUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;

@Getter
public class MeetingResponse {
    private final long id;
    private final String thumbnailUrl;
    private final List<String> hashtags;

    @JsonProperty("info")
    private final InfoOutput infoOutput;

    @JsonProperty("location")
    private final LocationOutput locationOutput;

    @JsonProperty("time")
    private final TimeOutput timeOutput;

    @JsonProperty("host")
    private final HostOutput hostOutput;

    @QueryProjection
    public MeetingResponse(long id, String thumbnailUrl, String hashtags, InfoOutput infoOutput,
                           LocationOutput locationOutput, TimeOutput timeOutput, HostOutput hostOutput) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
        this.hashtags = StringUtil.split(hashtags);
        this.infoOutput = infoOutput;
        this.locationOutput = locationOutput;
        this.timeOutput = timeOutput;
        this.hostOutput = hostOutput;
    }
}
