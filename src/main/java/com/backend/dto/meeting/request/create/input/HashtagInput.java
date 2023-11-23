package com.backend.dto.meeting.request.create.input;

import com.backend.dto.meeting.dto.HashtagDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class HashtagInput extends HashtagDTO {
    @JsonCreator
    public static HashtagInput create(List<String> hashtags) {
        return HashtagInput.builder()
                .hashtags(hashtags)
                .build();
    }
}


