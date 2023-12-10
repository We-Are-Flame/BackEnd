package com.backend.service.notification;

import com.backend.entity.notification.NotificationType;

public class NotificationFormatter {
    public static String format(NotificationType type, String meetingName) {
        return switch (type) {
            case MEETING_REQUEST -> meetingName + "에 참가신청이 들어왔습니다.";
            case MEETING_ACCEPTED -> meetingName + "의 참여가 수락되었습니다.";
            case MEETING_REJECTED -> meetingName + "의 참여가 거절되었습니다.";
            default -> throw new IllegalArgumentException("Unknown notification type");
        };
    }
}

