package com.backend.repository.meeting.meeting;

import com.backend.dto.meeting.response.MeetingDetailResponse;
import com.backend.dto.meeting.response.MeetingResponse;
import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.entity.chat.QChatMessage;
import com.backend.entity.chat.QChatRoom;
import com.backend.entity.chat.QChatRoomUser;
import com.backend.entity.meeting.QComment;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.meeting.QMeetingImage;
import com.backend.entity.meeting.QMeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public Page<MeetingResponse> findAllWithDetails(Pageable pageable) {
        List<MeetingResponse> meetings = queryBuilder.createAllMeetingQuery(pageable);

        long total = Optional.ofNullable(queryFactory.select(QMeeting.meeting.count())
                        .from(QMeeting.meeting)
                        .fetchOne())
                .orElse(0L);

        return new PageImpl<>(meetings, pageable, total);
    }

    @Override
    public Optional<MeetingDetailResponse> findMeetingWithDetailsById(Long meetingId) {
        MeetingDetailResponse meeting = queryBuilder.createDetailMeetingQuery(meetingId);
        return Optional.of(meeting);
    }

    @Override
    public List<MyMeetingResponse> findAllByHost(User host) {
        QMeeting qMeeting = QMeeting.meeting;

        return queryBuilder.createBaseMyMeetingQuery()
                .where(qMeeting.host.eq(host))
                .fetch();
    }

    @Override
    public List<MyMeetingResponse> findAllParticipatedByUser(User user) {
        QMeeting qMeeting = QMeeting.meeting;
        QMeetingRegistration qMeetingRegistration = QMeetingRegistration.meetingRegistration;

        return queryBuilder.createBaseMyMeetingQuery()
                .leftJoin(qMeeting.registrations, qMeetingRegistration)
                .where(qMeetingRegistration.user.eq(user)
                        .and(qMeetingRegistration.status.eq(RegistrationStatus.ACCEPTED))
                        .and(qMeetingRegistration.role.eq(RegistrationRole.MEMBER)))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteMeetingWithAllDetails(Long meetingId) {
        Long chatRoomId = queryFactory.select(QChatRoom.chatRoom.id)
                .from(QChatRoom.chatRoom)
                .where(QChatRoom.chatRoom.meeting.id.eq(meetingId))
                .fetchOne();

        if (chatRoomId != null) {
            queryFactory.delete(QChatMessage.chatMessage)
                    .where(QChatMessage.chatMessage.chatRoom.id.eq(chatRoomId))
                    .execute();
            queryFactory.delete(QChatRoomUser.chatRoomUser)
                    .where(QChatRoomUser.chatRoomUser.chatRoom.id.eq(chatRoomId))
                    .execute();
            queryFactory.delete(QChatRoom.chatRoom)
                    .where(QChatRoom.chatRoom.meeting.id.eq(meetingId))
                    .execute();
        }

        queryFactory.delete(QMeetingImage.meetingImage)
                .where(QMeetingImage.meetingImage.meeting.id.eq(meetingId))
                .execute();
        queryFactory.delete(QMeetingHashtag.meetingHashtag)
                .where(QMeetingHashtag.meetingHashtag.meeting.id.eq(meetingId))
                .execute();
        queryFactory.delete(QMeetingRegistration.meetingRegistration)
                .where(QMeetingRegistration.meetingRegistration.meeting.id.eq(meetingId))
                .execute();
        queryFactory.delete(QComment.comment)
                .where(QComment.comment.meeting.id.eq(meetingId))
                .execute();

        queryFactory.delete(QMeeting.meeting)
                .where(QMeeting.meeting.id.eq(meetingId))
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
}
