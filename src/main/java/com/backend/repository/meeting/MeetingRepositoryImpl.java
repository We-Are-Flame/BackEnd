package com.backend.repository.meeting;

import com.backend.entity.meeting.Hashtag;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingHashtag;
import com.backend.entity.meeting.QHashtag;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.meeting.QMeetingImage;
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
        JPAQuery<Meeting> query = createBaseMeetingQuery()
                .orderBy(getOrderSpecifiers(pageable));

        applyPagination(query, pageable);

        List<Meeting> meetings = query.fetch().stream()
                .map(this::enrichMeetingWithHashtags)
                .collect(Collectors.toList());
        long total = fetchMeetingCount();

        return new PageImpl<>(meetings, pageable, total);
    }

    public Optional<Meeting> findMeetingWithDetailsById(Long meetingId) {
        JPAQuery<Meeting> query = createBaseMeetingQuery()
                .where(QMeeting.meeting.id.eq(meetingId))
                .leftJoin(QMeeting.meeting.meetingImages, QMeetingImage.meetingImage).fetchJoin();

        return Optional.ofNullable(performQueryAndEnrichWithHashtags(query));
    }

    private JPAQuery<Meeting> createBaseMeetingQuery() {
        return queryFactory
                .selectFrom(QMeeting.meeting)
                .leftJoin(QMeeting.meeting.host, QUser.user).fetchJoin()
                .leftJoin(QMeeting.meeting.meetingHashtags, QMeetingHashtag.meetingHashtag).fetchJoin()
                .leftJoin(QMeetingHashtag.meetingHashtag.hashtag, QHashtag.hashtag).fetchJoin();
    }

    private void applyPagination(JPAQuery<?> query, Pageable pageable) {
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return pageable.getSort().stream()
                .flatMap(order -> CustomSort.fromString(order.getProperty()).toOrderSpecifiers(QMeeting.meeting)
                        .stream())
                .toArray(OrderSpecifier[]::new);
    }

    private Meeting performQueryAndEnrichWithHashtags(JPAQuery<Meeting> query) {
        Meeting meeting = query.fetchOne();
        return enrichMeetingWithHashtags(meeting);
    }

    private Meeting enrichMeetingWithHashtags(Meeting meeting) {
        if (meeting != null) {
            Set<Hashtag> hashtags = extractHashtags(meeting);
            meeting.assignHashtags(hashtags);
        }
        return meeting;
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
