package com.backend.dto.meeting.request.create;

import com.backend.dto.meeting.request.create.input.HashtagInput;
import com.backend.dto.meeting.request.create.input.MeetingImageInput;
import com.backend.dto.meeting.request.create.input.InfoInput;
import com.backend.dto.meeting.request.create.input.LocationInput;
import com.backend.dto.meeting.request.create.input.TimeInput;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingCreateRequest {
    @Schema(example = "술", description = "카테고리")
    private final String category;

    @JsonProperty("info")
    private final InfoInput infoInput;

    @JsonUnwrapped
    private final HashtagInput hashtagInput;

    @JsonProperty("location")
    private final LocationInput locationInput;

    @JsonProperty("time")
    private final TimeInput timeInput;
    @JsonProperty("image")
    private final MeetingImageInput imageInput;

    @JsonIgnore
    public String getThumbnailUrl() {
        return imageInput.getThumbnailUrl();
    }
}
