package com.backend.dto.meeting.response;

import com.backend.dto.meeting.response.output.InfoOutput;
import com.backend.dto.meeting.response.output.LocationOutput;
import com.backend.dto.meeting.response.output.TimeOutput;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;

@Getter
public class MyMeetingResponse {
    private final long id;
    private final String thumbnailUrl;
    private final InfoOutput infoOutput;
    private final LocationOutput locationOutput;
    private final TimeOutput timeOutput;
    private TimeStatus status;
    private List<String> hashtags;

    @QueryProjection
    public MyMeetingResponse(long id, String thumbnailUrl, InfoOutput infoOutput,
                             LocationOutput locationOutput, TimeOutput timeOutput) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
        this.infoOutput = infoOutput;
        this.locationOutput = locationOutput;
        this.timeOutput = timeOutput;
    }

    public void assignHashtag(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public void assignTimeStatus(TimeOutput timeOutput) {
        this.status = TimeStatus.determineTimeStatus(timeOutput);
    }
}
