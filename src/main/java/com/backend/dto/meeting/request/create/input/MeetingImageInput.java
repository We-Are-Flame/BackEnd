package com.backend.dto.meeting.request.create.input;

import com.backend.dto.meeting.dto.MeetingImageDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MeetingImageInput extends MeetingImageDTO {
    @JsonCreator
    public static MeetingImageInput create(String thumbnailUrl, List<String> imageUrls) {
        return MeetingImageInput.builder()
                .thumbnailUrl(thumbnailUrl)
                .imageUrls(imageUrls)
                .build();
    }
}
