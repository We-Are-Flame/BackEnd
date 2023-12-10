package com.backend.repository.meeting.meeting;

import com.backend.dto.meeting.response.MeetingDetailResponse;
import com.backend.dto.meeting.response.MeetingResponse;
import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.entity.chat.QChatMessage;
import com.backend.entity.chat.QChatRoom;
import com.backend.entity.chat.QChatRoomUser;
import com.backend.entity.meeting.Category;
import com.backend.entity.meeting.QComment;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.meeting.QMeetingImage;
import com.backend.entity.meeting.QMeetingRegistration;
import com.backend.entity.meeting.RegistrationRole;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.user.User;
import com.backend.strategy.CustomSort;
import com.querydsl.jpa.impl.JPAQuery;
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
    public Page<MeetingResponse> findAllWithDetails(Pageable pageable, Category category) {
        JPAQuery<MeetingResponse> query = queryBuilder.createAllMeetingQuery();

        if (category != null) {
            query.where(QMeeting.meeting.category.name.eq(category.getName()));
        }

        return executeQueryWithPagination(query, pageable);
    }

    private long countTotalMeetings() {
        QMeeting qMeeting = QMeeting.meeting;
        return Optional.ofNullable(queryFactory.select(qMeeting.count())
                        .from(qMeeting)
                        .fetchOne())
                .orElse(0L);
    }

    @Override
    public Page<MeetingResponse> findByTitle(String title, Pageable pageable) {
        JPAQuery<MeetingResponse> query = queryBuilder.createAllMeetingQuery();
        query.where(QMeeting.meeting.title.containsIgnoreCase(title));

        return executeQueryWithPagination(query, pageable);
    }

    @Override
    public Page<MeetingResponse> findByHashtag(String hashtag, Pageable pageable) {
        JPAQuery<MeetingResponse> query = queryBuilder.createAllMeetingQuery();
        query.where(QMeetingHashtag.meetingHashtag.hashtag.name.containsIgnoreCase(hashtag));

        return executeQueryWithPagination(query, pageable);
    }

    private Page<MeetingResponse> executeQueryWithPagination(JPAQuery<MeetingResponse> query, Pageable pageable) {
        List<MeetingResponse> meetings = query
                .orderBy(CustomSort.createPageableSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = countTotalMeetings();
        return new PageImpl<>(meetings, pageable, total);
    }

    @Override
    public Optional<MeetingDetailResponse> findMeetingWithDetailsById(Long meetingId, Optional<Long> userId) {
        MeetingDetailResponse meeting = queryBuilder.createDetailMeetingQuery(meetingId, userId);
        return Optional.ofNullable(meeting);
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
}
