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
        QMeetingRegistration registration = QMeetingRegistration.meetingRegistration;
        QUser user = QUser.user;

        JPAQuery<Meeting> query = queryFactory
                .selectFrom(meeting)
                .leftJoin(meeting.category, QCategory.category).fetchJoin()
                .leftJoin(meeting.meetingHashtags, QMeetingHashtag.meetingHashtag).fetchJoin()
                .leftJoin(QMeetingHashtag.meetingHashtag.hashtag, QHashtag.hashtag).fetchJoin()
                .leftJoin(meeting.registrations, registration).fetchJoin()
                .leftJoin(registration.user, user).fetchJoin()
                .leftJoin(meeting.meetingImages, QMeetingImage.meetingImage).fetchJoin()
                .where(registration.role.eq(RegistrationRole.OWNER));

        // 정렬 및 페이지네이션 적용
        for (Sort.Order order : pageable.getSort()) {
            OrderSpecifier<?> orderSpecifier = getOrderedSpecifier(meeting, order);
            query.orderBy(orderSpecifier);
        }

        List<Meeting> meetings = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 각 Meeting 엔티티에 대해 호스트를 할당
        meetings.forEach(this::assignHostToMeeting);

        return meetings;
    }

    private void assignHostToMeeting(Meeting meeting) {
        meeting.getRegistrations().stream()
                .filter(registration -> registration.getRole() == RegistrationRole.OWNER)
                .findFirst()
                .ifPresent(ownerRegistration -> meeting.assignHost(ownerRegistration.getUser()));
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
