package com.backend.before.dto.meeting.response;

import com.backend.before.dto.meeting.response.output.InfoOutput;
import com.backend.before.dto.meeting.response.output.LocationOutput;
import com.backend.before.dto.meeting.response.output.TimeOutput;
import com.backend.before.util.etc.StringUtil;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;

@Getter
public class MyMeetingResponse {
    private final long id;
    private final String thumbnailUrl;
    private final List<String> hashtags;
    private final InfoOutput infoOutput;
    private final LocationOutput locationOutput;
    private final TimeOutput timeOutput;
    private TimeStatus status;

    @QueryProjection
    public MyMeetingResponse(long id, String thumbnailUrl, String hashtags, InfoOutput infoOutput,
                             LocationOutput locationOutput, TimeOutput timeOutput) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
        this.hashtags = StringUtil.split(hashtags);
        this.infoOutput = infoOutput;
        this.locationOutput = locationOutput;
        this.timeOutput = timeOutput;
    }

    public void assignTimeStatus(TimeOutput timeOutput) {
        this.status = TimeStatus.determineTimeStatus(timeOutput);
    }
}
