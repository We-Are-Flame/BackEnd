package com.backend.meeting.domain.meeting.repository;

import com.backend.before.dto.meeting.response.*;
import com.backend.before.entity.user.QUser;
import com.backend.meeting.common.type.FilteringType;
import com.backend.meeting.domain.category.entity.Category;
import com.backend.meeting.domain.category.entity.QCategory;
import com.backend.meeting.domain.hashtag.entity.QHashtag;
import com.backend.meeting.domain.hashtag.entity.QMeetingHashtag;
import com.backend.meeting.domain.image.entity.QMeetingImage;
import com.backend.meeting.domain.meeting.entity.QMeeting;
import com.backend.meeting.common.mapper.ProjectionMapper;
import com.backend.registration.entity.QMeetingRegistration;
import com.backend.registration.entity.RegistrationRole;
import com.backend.registration.entity.RegistrationStatus;

import com.backend.before.entity.user.User;
import com.backend.meeting.common.type.SortType;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingQueryBuilder {
    private static final String HASHTAGS = "hashtags";
    private static final String IMAGE_URLS = "imageUrls";
    private static final String GROUP_CONCAT_DISTINCT = "GROUP_CONCAT(DISTINCT {0})";
    private final JPAQueryFactory queryFactory;

    public List<MeetingResponse> createAllMeetingQuery(Pageable pageable, Category category) {
        QMeeting qMeeting = QMeeting.meeting;
        QCategory qCategory = QCategory.category;
        QUser qUser = QUser.user;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;
        QHashtag qHashtag = QHashtag.hashtag;

        JPAQuery<Long> subQuery = queryFactory
                .select(qMeeting.id)
                .from(qMeeting)
                .innerJoin(qMeeting.category, qCategory);

        if (category != null) {
            subQuery.where(qCategory.name.eq(category.getName()));
        }

        List<Long> meetingIds = subQuery
                .orderBy(SortType.createPageableSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return queryFactory
                .select(ProjectionMapper.toMeetingResponse(qMeeting, qUser))
                .from(qMeeting)
                .innerJoin(qMeeting.host, qUser)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .where(qMeeting.id.in(meetingIds))
                .groupBy(qMeeting.id, qUser.nickname, qUser.profileImage, qUser.schoolEmail)
                .fetch();
    }

    public List<MeetingResponse> createFilterMeetingQuery(FilteringType type, String filterValue, Pageable pageable) {
        QMeeting qMeeting = QMeeting.meeting;
        QUser qUser = QUser.user;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;
        QHashtag qHashtag = QHashtag.hashtag;
        QCategory qCategory = QCategory.category;

        JPAQuery<Long> subQuery = queryFactory
                .select(qMeeting.id)
                .from(qMeeting)
                .leftJoin(qMeeting.category, qCategory)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .where(type.createWhereClause(filterValue))
                .orderBy(SortType.createPageableSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Long> meetingIds = subQuery.fetch();

        return queryFactory
                .select(ProjectionMapper.toMeetingResponse(qMeeting, qUser))
                .from(qMeeting)
                .innerJoin(qMeeting.host, qUser)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .where(qMeeting.id.in(meetingIds))
                .groupBy(qMeeting.id, qUser.nickname, qUser.profileImage, qUser.schoolEmail)
                .fetch();
    }

    public JPAQuery<Long> createCountQuery(Category category) {
        QMeeting qMeeting = QMeeting.meeting;
        QCategory qCategory = QCategory.category;

        JPAQuery<Long> query = queryFactory
                .select(qMeeting.count())
                .from(qMeeting)
                .innerJoin(qMeeting.category, qCategory);

        if (category != null) {
            query.where(qCategory.name.eq(category.getName()));
        }

        return query;
    }

    public Long createFilterCountQuery(FilteringType type, String filterValue) {
        QMeeting qMeeting = QMeeting.meeting;
        QCategory qCategory = QCategory.category;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;
        QHashtag qHashtag = QHashtag.hashtag;

        return queryFactory
                .select(qMeeting.countDistinct())
                .from(qMeeting)
                .leftJoin(qMeeting.category, qCategory)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .where(type.createWhereClause(filterValue))
                .fetchOne();
    }


    public JPAQuery<Long> createCountQueryByTitle(String title) {
        QMeeting qMeeting = QMeeting.meeting;

        return queryFactory
                .select(qMeeting.count())
                .from(qMeeting)
                .where(qMeeting.title.containsIgnoreCase(title));
    }

    public JPAQuery<Long> createCountQueryByHashtag(String hashtag) {
        QMeeting qMeeting = QMeeting.meeting;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;
        QHashtag qHashtag = QHashtag.hashtag;

        return queryFactory
                .select(qMeeting.countDistinct())
                .from(qMeeting)
                .join(qMeeting.meetingHashtags, qMeetingHashtag)
                .join(qMeetingHashtag.hashtag, qHashtag)
                .where(qHashtag.name.containsIgnoreCase(hashtag));
    }

    public MeetingDetailResponse createDetailMeetingQuery(Long meetingId, Optional<Long> userId) {
        QUser qUser = QUser.user;
        QMeeting qMeeting = QMeeting.meeting;
        QHashtag qHashtag = QHashtag.hashtag;
        QCategory qCategory = QCategory.category;
        QMeetingImage qMeetingImage = QMeetingImage.meetingImage;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;
        QMeetingRegistration qMeetingRegistration = QMeetingRegistration.meetingRegistration;

        JPQLQuery<MeetingDetailResponse> query = queryFactory
                .select(ProjectionMapper.toMeetingDetailResponse(qMeeting, qUser, qCategory, qMeetingImage, userId.isPresent() ? qMeetingRegistration : null))
                .from(qMeeting)
                .leftJoin(qMeeting.meetingImages, qMeetingImage)
                .leftJoin(qMeeting.host, qUser)
                .leftJoin(qMeeting.category, qCategory)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .where(qMeeting.id.eq(meetingId));

        if (userId.isPresent()) {
            query = query.leftJoin(qMeeting.registrations, qMeetingRegistration)
                    .on(qMeetingRegistration.user.id.eq(userId.get()));
        }

        return query.groupBy(qMeeting.id)
                .fetchOne();
    }

    public List<MyMeetingResponse> createMyMeetingQueryByHost(User host) {
        QMeeting qMeeting = QMeeting.meeting;
        QHashtag qHashtag = QHashtag.hashtag;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;

        return queryFactory.select(ProjectionMapper.toMyMeetingResponse(qMeeting))
                .from(qMeeting)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .where(qMeeting.host.eq(host))
                .groupBy(qMeeting.id)
                .orderBy(SortType.getSoonOrderSpecifiers())
                .fetch();
    }

    public List<MyMeetingResponse> createMyMeetingQueryByParticipant(User user) {
        QMeeting qMeeting = QMeeting.meeting;
        QHashtag qHashtag = QHashtag.hashtag;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;
        QMeetingRegistration qMeetingRegistration = QMeetingRegistration.meetingRegistration;

        return queryFactory.select(ProjectionMapper.toMyMeetingResponse(qMeeting))
                .from(qMeeting)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .join(qMeeting.registrations, qMeetingRegistration)
                .where(qMeetingRegistration.user.eq(user)
                        .and(qMeetingRegistration.status.eq(RegistrationStatus.ACCEPTED))
                        .and(qMeetingRegistration.role.eq(RegistrationRole.MEMBER)))
                .groupBy(qMeeting.id)
                .orderBy(SortType.getSoonOrderSpecifiers())
                .fetch();
    }
}