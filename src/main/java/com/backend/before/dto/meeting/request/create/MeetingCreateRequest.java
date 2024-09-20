package com.backend.before.dto.meeting.request.create;

import com.backend.before.dto.meeting.request.create.input.HashtagInput;
import com.backend.before.dto.meeting.request.create.input.InfoInput;
import com.backend.before.dto.meeting.request.create.input.LocationInput;
import com.backend.before.dto.meeting.request.create.input.MeetingImageInput;
import com.backend.before.dto.meeting.request.create.input.TimeInput;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

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

    @JsonIgnore
    public List<String> getHashtags() {
        if (hashtagInput == null) {
            return Collections.emptyList();
        }
        return hashtagInput.getHashtags();
    }
}
