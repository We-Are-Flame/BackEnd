package com.backend.repository.meeting.hashtag;

import com.backend.dto.meeting.response.MyMeetingResponse;
import java.util.List;

public interface HashtagRepositoryCustom {
    void findHashtagByMeetings(List<MyMeetingResponse> meetings);
}
