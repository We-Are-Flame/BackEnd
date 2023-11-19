package com.backend.dto.meeting.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HashtagDTO {
    private final List<String> hashtags;

    @JsonCreator
    public HashtagDTO(@JsonProperty("hashtags") List<String> hashtags) {
        this.hashtags = hashtags;
    }
}
