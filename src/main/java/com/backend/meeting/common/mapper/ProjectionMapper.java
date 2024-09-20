package com.backend.meeting.common.mapper;

import com.backend.before.dto.meeting.response.QMeetingDetailResponse;
import com.backend.before.dto.meeting.response.QMeetingResponse;
import com.backend.before.dto.meeting.response.QMyMeetingResponse;
import com.backend.before.dto.meeting.response.output.*;
import com.backend.before.entity.user.QUser;
import com.backend.meeting.domain.category.entity.QCategory;
import com.backend.meeting.domain.hashtag.entity.QMeetingHashtag;
import com.backend.meeting.domain.image.entity.QMeetingImage;
import com.backend.meeting.domain.meeting.entity.QMeeting;
import com.backend.registration.entity.QMeetingRegistration;
import com.backend.registration.entity.RegistrationRole;
import com.backend.registration.entity.RegistrationStatus;
import com.querydsl.core.types.dsl.Expressions;

public class ProjectionMapper {
    private static final String GROUP_CONCAT_DISTINCT = "GROUP_CONCAT(DISTINCT {0})";

    public static QMeetingResponse toMeetingResponse(QMeeting qMeeting, QUser qUser) {
        return new QMeetingResponse(
                qMeeting.id,
                qMeeting.thumbnailUrl,
                Expressions.stringTemplate(GROUP_CONCAT_DISTINCT, QMeetingHashtag.meetingHashtag.hashtag.name),
                mapToInfoOutput(qMeeting),
                mapToLocationOutput(qMeeting),
                mapToTimeOutput(qMeeting),
                new QHostOutput(
                        qUser.nickname,
                        qUser.profileImage,
                        qUser.schoolEmail
                )
        );
    }

    public static QMyMeetingResponse toMyMeetingResponse(QMeeting qMeeting) {
        return new QMyMeetingResponse(
                qMeeting.id,
                qMeeting.thumbnailUrl,
                Expressions.stringTemplate(GROUP_CONCAT_DISTINCT, QMeetingHashtag.meetingHashtag.hashtag.name),
                mapToInfoOutput(qMeeting),
                mapToLocationOutput(qMeeting),
                mapToTimeOutput(qMeeting)
        );
    }

    public static QMeetingDetailResponse toMeetingDetailResponse(QMeeting qMeeting, QUser qUser, QCategory qCategory, QMeetingImage qMeetingImage, QMeetingRegistration qMeetingRegistration) {
        return new QMeetingDetailResponse(
                qMeeting.id,
                qCategory.name,
                Expressions.stringTemplate(GROUP_CONCAT_DISTINCT, QMeetingHashtag.meetingHashtag.hashtag.name).as("hashtags"),
                new QDetailInfoOutput(
                        qMeeting.title,
                        qMeeting.description,
                        qMeeting.maxParticipants,
                        qMeeting.currentParticipants
                ),
                new QMeetingImageOutput(
                        qMeeting.thumbnailUrl,
                        Expressions.stringTemplate(GROUP_CONCAT_DISTINCT, qMeetingImage.imageUrl).as("imageUrls")
                ),
                mapToLocationOutput(qMeeting),
                new QDetailTimeOutput(
                        qMeeting.meetingTime.startTime,
                        qMeeting.meetingTime.endTime,
                        qMeeting.createdAt,
                        qMeeting.meetingTime.duration
                ),
                new QHostOutput(
                        qUser.nickname,
                        qUser.profileImage,
                        qUser.schoolEmail
                ),
                qMeetingRegistration != null ? qMeetingRegistration.status : Expressions.constant(RegistrationStatus.NONE),
                qMeetingRegistration != null ? qMeetingRegistration.role : Expressions.constant(RegistrationRole.MEMBER)
        );
    }

    private static QInfoOutput mapToInfoOutput(QMeeting qMeeting) {
        return new QInfoOutput(
                qMeeting.title,
                qMeeting.maxParticipants,
                qMeeting.currentParticipants
        );
    }

    private static QLocationOutput mapToLocationOutput(QMeeting qMeeting) {
        return new QLocationOutput(
                qMeeting.meetingAddress.location,
                qMeeting.meetingAddress.detailLocation,
                qMeeting.meetingAddress.latitude,
                qMeeting.meetingAddress.longitude
        );
    }

    private static QTimeOutput mapToTimeOutput(QMeeting qMeeting) {
        return new QTimeOutput(
                qMeeting.meetingTime.startTime,
                qMeeting.meetingTime.endTime,
                qMeeting.meetingTime.duration
        );
    }
}