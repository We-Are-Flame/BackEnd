package com.backend.repository.meeting.meeting;

import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.entity.chat.QChatMessage;
import com.backend.entity.chat.QChatRoom;
import com.backend.entity.chat.QChatRoomUser;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.QCategory;
import com.backend.entity.meeting.QComment;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.meeting.QMeetingImage;
import com.backend.entity.meeting.QMeetingRegistration;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import com.backend.repository.meeting.hashtag.HashtagRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final HashtagRepository hashtagRepository;
    private final MeetingQueryBuilder queryBuilder;

    @Override
    public Page<Meeting> findAllWithDetails(Pageable pageable) {
        JPAQuery<Meeting> query = queryBuilder.createBaseMeetingQuery()
                .orderBy(CustomSort.createPageableSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Meeting> meetings = query.fetch().stream()
                .peek(Meeting::enrichWithHashtags)
                .collect(Collectors.toList());

        long total = Optional.ofNullable(queryFactory.select(QMeeting.meeting.count())
                        .from(QMeeting.meeting)
                        .fetchOne())
                .orElse(0L);

        return new PageImpl<>(meetings, pageable, total);
    }

    @Override
    public Optional<Meeting> findMeetingWithDetailsById(Long meetingId) {
        Meeting meeting = queryBuilder.createBaseMeetingQuery()
                .leftJoin(QMeeting.meeting.meetingImages, QMeetingImage.meetingImage).fetchJoin()
                .leftJoin(QMeeting.meeting.registrations, QMeetingRegistration.meetingRegistration).fetchJoin()
                .leftJoin(QMeeting.meeting.category, QCategory.category).fetchJoin()
                .where(QMeeting.meeting.id.eq(meetingId))
                .fetchOne();

        assert meeting != null;

        meeting.enrichWithHashtags();

        return Optional.of(meeting);
    }

    @Override
    public List<MyMeetingResponse> findAllByHost(User host) {
        QMeeting qMeeting = QMeeting.meeting;

        List<MyMeetingResponse> meetings = queryBuilder.createBaseMyMeetingQuery()
                .where(qMeeting.host.eq(host))
                .fetch();

        hashtagRepository.findHashtagByMeetings(meetings);

        return meetings;
    }

    @Override
    public List<MyMeetingResponse> findAllParticipatedByUser(User user) {
        QMeeting qMeeting = QMeeting.meeting;
        QMeetingRegistration qMeetingRegistration = QMeetingRegistration.meetingRegistration;

        List<MyMeetingResponse> meetings = queryBuilder.createBaseMyMeetingQuery()
                .leftJoin(qMeeting.registrations, qMeetingRegistration)
                .where(qMeetingRegistration.user.eq(user)
                        .and(qMeetingRegistration.status.eq(RegistrationStatus.ACCEPTED)))
                .fetch();

        hashtagRepository.findHashtagByMeetings(meetings);

        return meetings;
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
        log.info("isOwner called with meetingId: {}, userId: {}", meetingId, userId);
        QMeeting meeting = QMeeting.meeting;

        return queryFactory
                .select(meeting.id)
                .from(meeting)
                .where(meeting.id.eq(meetingId)
                        .and(meeting.host.id.eq(userId)))
                .fetchOne() != null;
    }
}
