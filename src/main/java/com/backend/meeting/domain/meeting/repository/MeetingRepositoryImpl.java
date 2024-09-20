package com.backend.meeting.domain.meeting.repository;

import com.backend.before.dto.meeting.response.MeetingDetailResponse;
import com.backend.before.dto.meeting.response.MeetingResponse;
import com.backend.before.dto.meeting.response.MyMeetingResponse;
import com.backend.before.entity.chat.QChatMessage;
import com.backend.before.entity.chat.QChatRoom;
import com.backend.before.entity.chat.QChatRoomUser;
import com.backend.before.entity.user.User;
import com.backend.comment.entity.QComment;
import com.backend.meeting.common.type.FilteringType;
import com.backend.meeting.domain.category.entity.Category;
import com.backend.meeting.domain.hashtag.entity.QMeetingHashtag;
import com.backend.meeting.domain.image.entity.QMeetingImage;
import com.backend.meeting.domain.meeting.entity.Meeting;
import com.backend.meeting.domain.meeting.entity.QMeeting;
import com.backend.registration.entity.QMeetingRegistration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MeetingRepositoryImpl implements MeetingRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final MeetingQueryBuilder queryBuilder;

    @Override
    public Page<MeetingResponse> findAllWithDetails(Pageable pageable, Category category) {
        List<MeetingResponse> meetings = queryBuilder.createAllMeetingQuery(pageable, category);
        long total = queryBuilder.createCountQuery(category).fetchOne();
        return new PageImpl<>(meetings, pageable, total);
    }

    @Override
    public Page<MeetingResponse> findMeetingsWithFilter(FilteringType type, String filterValue, Pageable pageable) {
        List<MeetingResponse> meetings = queryBuilder.createFilterMeetingQuery(type, filterValue, pageable);
        long total = queryBuilder.createFilterCountQuery(type, filterValue);
        return new PageImpl<>(meetings, pageable, total);
    }

    @Override
    public Optional<MeetingDetailResponse> findMeetingWithDetailsById(Long meetingId, Optional<Long> userId) {
        MeetingDetailResponse meeting = queryBuilder.createDetailMeetingQuery(meetingId, userId);
        return Optional.ofNullable(meeting);
    }

    @Override
    public List<MyMeetingResponse> findAllByHost(User host) {
        return queryBuilder.createMyMeetingQueryByHost(host);
    }

    @Override
    public List<MyMeetingResponse> findAllParticipatedByUser(User user) {
        return queryBuilder.createMyMeetingQueryByParticipant(user);
    }

    @Override
    @Transactional
    public void deleteMeetingWithAllDetails(Long meetingId) {
        QChatRoom qChatRoom = QChatRoom.chatRoom;
        QChatMessage qChatMessage = QChatMessage.chatMessage;
        QChatRoomUser qChatRoomUser = QChatRoomUser.chatRoomUser;
        QMeetingImage qMeetingImage = QMeetingImage.meetingImage;
        QMeetingHashtag qMeetingHashtag = QMeetingHashtag.meetingHashtag;
        QMeetingRegistration qMeetingRegistration = QMeetingRegistration.meetingRegistration;
        QComment qComment = QComment.comment;
        QMeeting qMeeting = QMeeting.meeting;

        Long chatRoomId = queryFactory.select(qChatRoom.id)
                .from(qChatRoom)
                .where(qChatRoom.meeting.id.eq(meetingId))
                .fetchOne();

        if (chatRoomId != null) {
            queryFactory.delete(qChatMessage)
                    .where(qChatMessage.chatRoom.id.eq(chatRoomId))
                    .execute();
            queryFactory.delete(qChatRoomUser)
                    .where(qChatRoomUser.chatRoom.id.eq(chatRoomId))
                    .execute();
            queryFactory.delete(qChatRoom)
                    .where(qChatRoom.meeting.id.eq(meetingId))
                    .execute();
        }

        queryFactory.delete(qMeetingImage)
                .where(qMeetingImage.meeting.id.eq(meetingId))
                .execute();
        queryFactory.delete(qMeetingHashtag)
                .where(qMeetingHashtag.meeting.id.eq(meetingId))
                .execute();
        queryFactory.delete(qMeetingRegistration)
                .where(qMeetingRegistration.meeting.id.eq(meetingId))
                .execute();
        queryFactory.delete(qComment)
                .where(qComment.meeting.id.eq(meetingId))
                .execute();
        queryFactory.delete(qMeeting)
                .where(qMeeting.id.eq(meetingId))
                .execute();
    }

    @Override
    public boolean isOwner(Long meetingId, Long userId) {
        QMeeting meeting = QMeeting.meeting;

        return queryFactory
                .select(meeting.id)
                .from(meeting)
                .where(meeting.id.eq(meetingId)
                        .and(meeting.host.id.eq(userId)))
                .fetchOne() != null;
    }

    @Override
    public List<Meeting> findForEvaluation(LocalDateTime endTime) {
        QMeeting qMeeting = QMeeting.meeting;

        return queryFactory
                .selectFrom(qMeeting)
                .where(qMeeting.meetingTime.endTime.before(endTime)
                        .and(qMeeting.isEvaluated.eq(false)))
                .fetch();
    }
}