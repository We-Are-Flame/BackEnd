package com.backend.repository.meeting;

import com.backend.entity.meeting.Hashtag;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingHashtag;
import com.backend.entity.meeting.QHashtag;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.user.QUser;
import com.backend.service.meeting.CustomSort;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Meeting> findAllWithDetails(Pageable pageable) {
        List<Meeting> meetings = fetchMeetingsWithDetails(pageable);
        long total = fetchMeetingCount();
        return new PageImpl<>(meetings, pageable, total);
    }

    private List<Meeting> fetchMeetingsWithDetails(Pageable pageable) {
        JPAQuery<Meeting> query = createMeetingQuery(pageable);
        applyPagination(query, pageable);
        List<Meeting> meetings = query.fetch();
        return enrichMeetingsWithHashtags(meetings);
    }

    private JPAQuery<Meeting> createMeetingQuery(Pageable pageable) {
        QMeeting meeting = QMeeting.meeting;
        return queryFactory
                .select(meeting)
                .from(meeting)
                .leftJoin(meeting.host, QUser.user).fetchJoin()
                .leftJoin(meeting.meetingHashtags, QMeetingHashtag.meetingHashtag).fetchJoin()
                .leftJoin(QMeetingHashtag.meetingHashtag.hashtag, QHashtag.hashtag).fetchJoin()
                .orderBy(getOrderSpecifiers(pageable, meeting));
    }

    private void applyPagination(JPAQuery<?> query, Pageable pageable) {
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QMeeting meeting) {
        return pageable.getSort().stream()
                .flatMap(order -> CustomSort.fromString(order.getProperty()).toOrderSpecifiers(meeting).stream())
                .toArray(OrderSpecifier[]::new);
    }


    private List<Meeting> enrichMeetingsWithHashtags(List<Meeting> meetings) {
        meetings.forEach(meeting -> {
            Set<Hashtag> hashtags = extractHashtags(meeting);
            meeting.assignHashtags(hashtags);
        });
        return meetings;
    }

    private Set<Hashtag> extractHashtags(Meeting meeting) {
        return meeting.getMeetingHashtags().stream()
                .map(MeetingHashtag::getHashtag)
                .collect(Collectors.toSet());
    }

    private long fetchMeetingCount() {
        return Optional.ofNullable(queryFactory.select(QMeeting.meeting.count())
                        .from(QMeeting.meeting)
                        .fetchOne())
                .orElse(0L);
    }
}
