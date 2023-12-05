package com.backend.repository.meeting.meeting;

import com.backend.dto.meeting.response.MyMeetingResponse;
import com.backend.dto.meeting.response.QMyMeetingResponse;
import com.backend.dto.meeting.response.output.QInfoOutput;
import com.backend.dto.meeting.response.output.QLocationOutput;
import com.backend.dto.meeting.response.output.QTimeOutput;
import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.QHashtag;
import com.backend.entity.meeting.QMeeting;
import com.backend.entity.meeting.QMeetingHashtag;
import com.backend.entity.user.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeetingQueryBuilder {
    private final JPAQueryFactory queryFactory;

    public JPAQuery<MyMeetingResponse> createBaseMyMeetingQuery() {
        QMeeting qMeeting = QMeeting.meeting;
        // 모든 참여한 모임을 조회
        return queryFactory.select(new QMyMeetingResponse(
                        qMeeting.id,
                        qMeeting.thumbnailUrl,
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
                .orderBy(qMeeting.meetingTime.startTime.desc());
    }

    public JPAQuery<Meeting> createBaseMeetingQuery() {
        return queryFactory
                .selectFrom(QMeeting.meeting)
                .leftJoin(QMeeting.meeting.host, QUser.user).fetchJoin()
                .leftJoin(QMeeting.meeting.meetingHashtags, QMeetingHashtag.meetingHashtag).fetchJoin()
                .leftJoin(QMeetingHashtag.meetingHashtag.hashtag, QHashtag.hashtag).fetchJoin();
    }
}
