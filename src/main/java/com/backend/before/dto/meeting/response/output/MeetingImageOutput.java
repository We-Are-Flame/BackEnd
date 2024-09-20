package com.backend.before.dto.meeting.response.output;

import com.backend.before.dto.meeting.dto.MeetingImageDTO;
import com.backend.before.util.etc.StringUtil;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MeetingImageOutput extends MeetingImageDTO {
    @QueryProjection
    public MeetingImageOutput(String thumbnailUrl, String imageUrls) {
        super(thumbnailUrl, StringUtil.split(imageUrls));
    }
}
