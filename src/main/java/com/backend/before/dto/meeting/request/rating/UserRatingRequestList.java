package com.backend.before.dto.meeting.request.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRatingRequestList {
    @JsonProperty("rating_list")
    List<UserRatingRequest> userRatingRequest;
}
