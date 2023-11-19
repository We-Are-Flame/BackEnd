package com.backend.repository.meeting;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.QCategory;
import com.backend.entity.meeting.QHashtag;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.meeting.QMeetingImage;
import com.backend.entity.meeting.QMeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.user.QUser;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
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
        List<Meeting> meetings = fetchMeetingsWithDetails(pageable);
        long total = fetchMeetingCount();

        return new PageImpl<>(meetings, pageable, total);
    }

    private List<Meeting> fetchMeetingsWithDetails(Pageable pageable) {
        QMeeting meeting = QMeeting.meeting;

        JPAQuery<Meeting> query = queryFactory
                .selectFrom(meeting)
                .leftJoin(meeting.category, QCategory.category).fetchJoin()
                .leftJoin(meeting.meetingHashtags, QMeetingHashtag.meetingHashtag).fetchJoin()
                .leftJoin(QMeetingHashtag.meetingHashtag.hashtag, QHashtag.hashtag).fetchJoin()
                .leftJoin(meeting.registrations, QMeetingRegistration.meetingRegistration).fetchJoin()
                .leftJoin(QMeetingRegistration.meetingRegistration.user, QUser.user).fetchJoin()
                .leftJoin(meeting.meetingImages, QMeetingImage.meetingImage).fetchJoin()
                .where(QMeetingRegistration.meetingRegistration.role.eq(RegistrationRole.OWNER));

        for (Sort.Order order : pageable.getSort()) {
            OrderSpecifier<?> orderSpecifier = getOrderedSpecifier(meeting, order);
            query.orderBy(orderSpecifier);
        }

        return query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private long fetchMeetingCount() {
        return Optional.ofNullable(queryFactory.select(QMeeting.meeting.count())
                        .from(QMeeting.meeting)
                        .fetchOne())
                .orElse(0L);
    }

    private OrderSpecifier<?> getOrderedSpecifier(QMeeting meeting, Sort.Order order) {
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        return switch (order.getProperty()) {
            case "createdAt" -> new OrderSpecifier<>(direction, meeting.createdAt);
            case "title" -> new OrderSpecifier<>(direction, meeting.meetingInfo.title);
            default -> new OrderSpecifier<>(direction, meeting.id);
        };
    }
}
