package com.backend.repository.meeting.hashtag;

import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.entity.meeting.QHashtag;
import com.backend.entity.meeting.QMeetingHashtag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public void findHashtagByMeetings(List<MyMeetingResponse> meetings) {
        List<Long> meetingIds = meetings.stream()
                .map(MyMeetingResponse::getId)
                .toList();

        QHashtag qHashtag = QHashtag.hashtag;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;

        Map<Long, List<String>> hashtagsMap = queryFactory
                .select(qMeetingHashtag.meeting.id, qHashtag.name)
                .from(qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .where(qMeetingHashtag.meeting.id.in(meetingIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(qMeetingHashtag.meeting.id),
                        Collectors.mapping(tuple -> tuple.get(qHashtag.name), Collectors.toList())
                ));

        // 각 모임에 해시태그 목록 할당
        meetings.forEach(meetingResponse -> {
            List<String> hashtags = hashtagsMap.getOrDefault(meetingResponse.getId(), Collections.emptyList());
            meetingResponse.assignHashtag(hashtags);
        });
    }
}
