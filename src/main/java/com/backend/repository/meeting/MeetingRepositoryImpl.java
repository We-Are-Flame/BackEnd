package com.backend.repository.meeting;

import com.backend.entity.meeting.Hashtag;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingHashtag;
import com.backend.entity.meeting.QHashtag;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.user.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Meeting> findAllWithDetails(Pageable pageable) {
        CompletableFuture<List<Meeting>> meetingsFuture = CompletableFuture.supplyAsync(
                () -> fetchMeetingsWithDetails(pageable));
        CompletableFuture<Long> countFuture = CompletableFuture.supplyAsync(this::fetchMeetingCount);

        CompletableFuture.allOf(meetingsFuture, countFuture).join();

        try {
            List<Meeting> meetings = meetingsFuture.get();
            long total = countFuture.get();
            return new PageImpl<>(meetings, pageable, total);
        } catch (CompletionException e) {
            throw new RuntimeException("Meeting을 가져오는 중에 Error가 발생하였습니다.", e.getCause());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Meeting> fetchMeetingsWithDetails(Pageable pageable) {
        QMeeting meeting = QMeeting.meeting;
        QMeetingHashtag meetingHashtag = QMeetingHashtag.meetingHashtag;
        QHashtag hashtag = QHashtag.hashtag;

        JPAQuery<Meeting> query = queryFactory
                .select(meeting)
                .from(meeting)
                .leftJoin(meeting.host, QUser.user).fetchJoin()
                .leftJoin(meeting.meetingHashtags, meetingHashtag).fetchJoin()
                .leftJoin(meetingHashtag.hashtag, hashtag).fetchJoin();

        // 정렬 조건 추가
        for (Sort.Order order : pageable.getSort()) {
            query.orderBy(getOrderSpecifier(meeting, order));
        }

        // 페이징 처리
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Meeting> meetings = query.fetch();

        // 각 Meeting에 대해 Hashtag 목록을 할당
        meetings.forEach(m -> {
            Set<Hashtag> hashtags = m.getMeetingHashtags().stream()
                    .map(MeetingHashtag::getHashtag)
                    .collect(Collectors.toSet());
            m.assignHashtags(hashtags);
        });
        System.out.println(meetings);
        return meetings;
    }


    private OrderSpecifier<?> getOrderSpecifier(QMeeting meeting, Sort.Order order) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        return switch (order.getProperty()) {
            case "createdAt" -> new OrderSpecifier<>(direction, meeting.createdAt);
            case "title" -> new OrderSpecifier<>(direction, meeting.title);
            default -> new OrderSpecifier<>(direction, meeting.id);
        };
    }

    private long fetchMeetingCount() {
        return Optional.ofNullable(queryFactory.select(QMeeting.meeting.count())
                        .from(QMeeting.meeting)
                        .fetchOne())
                .orElse(0L);
    }
}
