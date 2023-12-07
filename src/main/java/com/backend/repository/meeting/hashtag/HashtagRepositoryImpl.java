package com.backend.repository.meeting.hashtag;

import com.backend.entity.meeting.QHashtag;
import com.backend.entity.meeting.QMeetingHashtag;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public Map<Long, List<String>> findHashtagByMeetings(List<Long> meetingIds) {
        QHashtag qHashtag = QHashtag.hashtag;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;

        return queryFactory
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
    }
}
