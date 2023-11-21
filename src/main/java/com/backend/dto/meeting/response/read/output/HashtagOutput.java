package com.backend.dto.meeting.response.read.output;

import com.backend.dto.meeting.dto.HashtagDTO;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class HashtagOutput extends HashtagDTO {
    public static HashtagOutput create(List<String> hashtags) {
        return HashtagOutput.builder().hashtags(hashtags).build();
    }
}
