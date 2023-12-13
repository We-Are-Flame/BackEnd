package com.backend.repository.meeting.hashtag;

import java.util.List;
import java.util.Map;

public interface HashtagRepositoryCustom {
    Map<Long, List<String>> findHashtagByMeetings(List<Long> meetingIds);
}
