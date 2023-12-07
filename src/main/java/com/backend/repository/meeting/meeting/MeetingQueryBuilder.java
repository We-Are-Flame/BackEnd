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
import com.backend.entity.user.QUser;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingQueryBuilder {
    private final JPAQueryFactory queryFactory;

    public List<MeetingResponse> createAllMeetingQuery(Pageable pageable) {
        QMeeting qMeeting = QMeeting.meeting;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;

        return queryFactory.select(new QMeetingResponse(
                        qMeeting.id,
                        qMeeting.thumbnailUrl,
                        groupConcatHashtags().as("hashtags"),
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
                                QUser.user.nickname,
                                QUser.user.profileImage
                        )
                ))
                .from(qMeeting)
                .leftJoin(qMeeting.host, QUser.user)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, QHashtag.hashtag)
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

    public MeetingDetailResponse createDetailMeetingQuery(Long meetingId) {
        QMeeting qMeeting = QMeeting.meeting;
        QHashtag qHashtag = QHashtag.hashtag;
        QMeetingImage qMeetingImage = QMeetingImage.meetingImage;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;

        return queryFactory
                .from(qMeeting)
                .leftJoin(qMeeting.meetingImages, qMeetingImage)
                .groupBy(qMeeting.id)
                .select(new QMeetingDetailResponse(
                        qMeeting.id,
                        QCategory.category.name,
                        groupConcatHashtags().as("hashtags"),
                        new QDetailInfoOutput(
                                qMeeting.title,
                                qMeeting.description,
                                qMeeting.maxParticipants,
                                qMeeting.currentParticipants
                        ),
                        new QMeetingImageOutput(
                                qMeeting.thumbnailUrl,
                                groupConcatImage().as("imageUrls")
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
                                QUser.user.nickname,
                                QUser.user.profileImage
                        )
                ))
                .leftJoin(qMeeting.host, QUser.user)
                .leftJoin(qMeeting.category, QCategory.category)
                .leftJoin(qMeeting.meetingHashtags, qMeetingHashtag)
                .leftJoin(qMeetingHashtag.hashtag, qHashtag)
                .where(QMeeting.meeting.id.eq(meetingId))
                .fetchOne();
    }

    public JPAQuery<MyMeetingResponse> createBaseMyMeetingQuery() {
        QMeeting qMeeting = QMeeting.meeting;
        QHashtag qHashtag = QHashtag.hashtag;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;

        return queryFactory.select(new QMyMeetingResponse(
                        qMeeting.id,
                        qMeeting.thumbnailUrl,
                        groupConcatHashtags().as(("hashtags")),
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
                "GROUP_CONCAT(DISTINCT {0})", QMeetingImage.meetingImage.imageUrl
        );
    }

    private StringTemplate groupConcatHashtags() {
        return Expressions.stringTemplate(
                "GROUP_CONCAT(DISTINCT {0})", QMeetingHashtag.meetingHashtag.hashtag.name
        );
    }
}
