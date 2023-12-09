package com.backend.repository.meeting.meeting;

import com.backend.dto.meeting.response.MeetingDetailResponse;
import com.backend.dto.meeting.response.MeetingResponse;
import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.dto.meeting.response.QMeetingDetailResponse;
import com.backend.dto.meeting.response.QMeetingResponse;
import com.backend.dto.meeting.response.QMyMeetingResponse;
import com.backend.dto.meeting.response.output.QDetailInfoOutput;
import com.backend.dto.meeting.response.output.QDetailTimeOutput;
import com.backend.dto.meeting.response.output.QHostOutput;
import com.backend.dto.meeting.response.output.QInfoOutput;
import com.backend.dto.meeting.response.output.QLocationOutput;
import com.backend.dto.meeting.response.output.QMeetingImageOutput;
import com.backend.dto.meeting.response.output.QTimeOutput;
import com.backend.entity.meeting.QCategory;
import com.backend.entity.meeting.QHashtag;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.meeting.QMeetingImage;
import com.backend.entity.meeting.QMeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
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

    private static JPQLQuery<MeetingDetailResponse> createJoinRegistration(Optional<Long> userId,
                                                                           QMeeting qMeeting,
                                                                           QMeetingRegistration qMeetingRegistration,
                                                                           JPQLQuery<MeetingDetailResponse> query) {
        if (userId.isPresent()) {
            query = query.leftJoin(qMeeting.registrations, qMeetingRegistration)
                    .on(qMeetingRegistration.user.id.eq(userId.get()))
                    .groupBy(qMeetingRegistration.status, qMeetingRegistration.role);
        }

        return query;
    }

    public List<MeetingResponse> createAllMeetingQuery(Pageable pageable) {
        QUser qUser = QUser.user;
        QMeeting qMeeting = QMeeting.meeting;
        QHashtag qHashtag = QHashtag.hashtag;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;

        return queryFactory.select(new QMeetingResponse(
                        qMeeting.id,
                        qMeeting.thumbnailUrl,
                        groupConcatHashtags().as(HASHTAGS),
                        new QInfoOutput(
                                qMeeting.title,
                                qMeeting.maxParticipants,
                                qMeeting.currentParticipants
                        ),
                        new QLocationOutput(
                                qMeeting.meetingAddress.location,
                                qMeeting.meetingAddress.detailLocation,
                                qMeeting.meetingAddress.latitude,
                                qMeeting.meetingAddress.longitude
                        ),
                        new QTimeOutput(
                                qMeeting.meetingTime.startTime,
                                qMeeting.meetingTime.endTime,
                                qMeeting.meetingTime.duration
                        ),
                        new QHostOutput(
                                qUser.nickname,
                                qUser.profileImage
                        )
                ))
                .from(qMeeting)
                .leftJoin(qMeeting.host, qUser)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .groupBy(qMeeting.id, qMeeting.thumbnailUrl, qMeeting.title, qMeeting.maxParticipants,
                        qMeeting.currentParticipants,
                        qMeeting.meetingAddress.location, qMeeting.meetingAddress.detailLocation,
                        qMeeting.meetingAddress.latitude,
                        qMeeting.meetingAddress.longitude, qMeeting.meetingTime.startTime, qMeeting.meetingTime.endTime,
                        qMeeting.meetingTime.duration,
                        qMeeting.host.nickname, qMeeting.host.profileImage)
                .orderBy(CustomSort.createPageableSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public MeetingDetailResponse createDetailMeetingQuery(Long meetingId, Optional<Long> userId) {
        QUser qUser = QUser.user;
        QMeeting qMeeting = QMeeting.meeting;
        QHashtag qHashtag = QHashtag.hashtag;
        QCategory qCategory = QCategory.category;
        QMeetingImage qMeetingImage = QMeetingImage.meetingImage;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;
        QMeetingRegistration qMeetingRegistration = QMeetingRegistration.meetingRegistration;

        BooleanBuilder whereClause = new BooleanBuilder(qMeeting.id.eq(meetingId));

        JPQLQuery<MeetingDetailResponse> query = queryFactory
                .select(new QMeetingDetailResponse(
                        qMeeting.id,
                        qCategory.name,
                        groupConcatHashtags().as(HASHTAGS),
                        new QDetailInfoOutput(
                                qMeeting.title,
                                qMeeting.description,
                                qMeeting.maxParticipants,
                                qMeeting.currentParticipants
                        ),
                        new QMeetingImageOutput(
                                qMeeting.thumbnailUrl,
                                groupConcatImage().as(IMAGE_URLS)
                        ),
                        new QLocationOutput(
                                qMeeting.meetingAddress.location,
                                qMeeting.meetingAddress.detailLocation,
                                qMeeting.meetingAddress.latitude,
                                qMeeting.meetingAddress.longitude
                        ),
                        new QDetailTimeOutput(
                                qMeeting.meetingTime.startTime,
                                qMeeting.meetingTime.endTime,
                                qMeeting.createdAt,
                                qMeeting.meetingTime.duration
                        ),
                        new QHostOutput(
                                qUser.nickname,
                                qUser.profileImage
                        ),
                        getRegistrationStatus(userId, qMeetingRegistration),
                        getRegistrationRole(userId, qMeetingRegistration)
                ))
                .from(qMeeting)
                .leftJoin(qMeeting.meetingImages, qMeetingImage)
                .leftJoin(qMeeting.host, qUser)
                .leftJoin(qMeeting.category, qCategory)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag);

        query = createJoinRegistration(userId, qMeeting, qMeetingRegistration, query);

        return query.where(whereClause)
                .groupBy(qMeeting.id, qCategory.name, qUser.nickname, qUser.profileImage,
                        qMeeting.thumbnailUrl, qMeeting.title, qMeeting.description,
                        qMeeting.maxParticipants, qMeeting.currentParticipants,
                        qMeeting.meetingAddress.location, qMeeting.meetingAddress.detailLocation,
                        qMeeting.meetingAddress.latitude, qMeeting.meetingAddress.longitude,
                        qMeeting.meetingTime.startTime, qMeeting.meetingTime.endTime, qMeeting.createdAt)
                .fetchOne();
    }

    private Expression<RegistrationRole> getRegistrationRole(Optional<Long> userId,
                                                             QMeetingRegistration qMeetingRegistration) {
        if (userId.isPresent()) {
            return qMeetingRegistration.role;
        } else {
            return Expressions.constant(RegistrationRole.MEMBER);
        }
    }

    private Expression<RegistrationStatus> getRegistrationStatus(Optional<Long> userId,
                                                                 QMeetingRegistration qMeetingRegistration) {
        if (userId.isPresent()) {
            return qMeetingRegistration.status;
        } else {
            return Expressions.constant(RegistrationStatus.NONE);
        }
    }

    public JPAQuery<MyMeetingResponse> createBaseMyMeetingQuery() {
        QMeeting qMeeting = QMeeting.meeting;
        QHashtag qHashtag = QHashtag.hashtag;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;

        return queryFactory.select(new QMyMeetingResponse(
                        qMeeting.id,
                        qMeeting.thumbnailUrl,
                        groupConcatHashtags().as((HASHTAGS)),
                        new QInfoOutput(
                                qMeeting.title,
                                qMeeting.maxParticipants,
                                qMeeting.currentParticipants
                        ),
                        new QLocationOutput(
                                qMeeting.meetingAddress.location,
                                qMeeting.meetingAddress.detailLocation,
                                qMeeting.meetingAddress.latitude,
                                qMeeting.meetingAddress.longitude
                        ),
                        new QTimeOutput(
                                qMeeting.meetingTime.startTime,
                                qMeeting.meetingTime.endTime,
                                qMeeting.meetingTime.duration
                        )
                ))
                .from(qMeeting)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .groupBy(qMeeting.id, qMeeting.thumbnailUrl, qMeeting.title, qMeeting.maxParticipants,
                        qMeeting.currentParticipants,
                        qMeeting.meetingAddress.location, qMeeting.meetingAddress.detailLocation,
                        qMeeting.meetingAddress.latitude,
                        qMeeting.meetingAddress.longitude, qMeeting.meetingTime.startTime, qMeeting.meetingTime.endTime,
                        qMeeting.meetingTime.duration)
                .orderBy(CustomSort.getSoonOrderSpecifiers());
    }

    private StringTemplate groupConcatImage() {
        return Expressions.stringTemplate(
                GROUP_CONCAT_DISTINCT, QMeetingImage.meetingImage.imageUrl
        );
    }

    private StringTemplate groupConcatHashtags() {
        return Expressions.stringTemplate(
                GROUP_CONCAT_DISTINCT, QMeetingHashtag.meetingHashtag.hashtag.name
        );
    }
}
