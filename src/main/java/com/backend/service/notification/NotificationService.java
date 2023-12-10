package com.backend.service.notification;

import com.backend.entity.meeting.Meeting;
import com.backend.entity.meeting.MeetingRegistration;
import com.backend.entity.meeting.RegistrationStatus;
import com.backend.entity.notification.Notification;
import com.backend.entity.notification.NotificationType;
import com.backend.entity.user.User;
import com.backend.repository.notification.EmittersRepository;
import com.backend.repository.notification.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final String CONNECTION_NAME = "notification";
    private final EmittersRepository emittersRepository;
    private final NotificationRepository notificationRepository;

    private static Notification buildNotification(User user, NotificationType type) {
        return Notification.builder()
                .user(user)
                .type(type)
                .isRead(false)
                .build();
    }

    public SseEmitter subscribe(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        emittersRepository.addEmitter(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emittersRepository.removeEmitter(userId));
        sseEmitter.onTimeout(() -> emittersRepository.removeEmitter(userId));
        sseEmitter.onError((e) -> emittersRepository.removeEmitter(userId));

        // 초기 데이터 전송
        try {
            Map<String, String> initialData = new HashMap<>();
            initialData.put("message", "Connected successfully");
            sseEmitter.send(SseEmitter.event().name(CONNECTION_NAME).data(initialData));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sseEmitter;
    }


    public void sendNotification(User user, Meeting meeting, NotificationType type) {
        Notification notification = buildNotification(user, type);
        notificationRepository.save(notification);

        SseEmitter emitter = emittersRepository.getEmitter(user.getId());

        if (emitter != null) {
            try {
                Map<String, String> data = new HashMap<>();
                data.put("notification_type", type.toString());
                data.put("nickname", user.getNickname());
                data.put("meeting_id", meeting.getId().toString());
                data.put("title", meeting.getTitle());
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonData = objectMapper.writeValueAsString(data);

                emitter.send(SseEmitter.event().name(type.toString()).data(jsonData));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendEvaluationRequest(Meeting meeting, User rater) {
        List<Long> userIds = fetchUserIds(meeting, rater);

        if (!userIds.isEmpty()) {
            SseEmitter emitter = emittersRepository.getEmitter(rater.getId());
            if (emitter != null) {
                try {
                    Map<String, Object> data = new HashMap<>();
                    data.put("meetingId", meeting.getId());
                    data.put("userIds", userIds);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonData = objectMapper.writeValueAsString(data);

                    emitter.send(SseEmitter.event()
                            .name(NotificationType.EVALUATE_REQUEST.toString())
                            .data(jsonData));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<Long> fetchUserIds(Meeting meeting, User rater) {
        return meeting.getRegistrations().stream()
                .filter(registration -> registration.getStatus() == RegistrationStatus.ACCEPTED)
                .map(MeetingRegistration::getUser)
                .map(User::getId)
                .filter(id -> !id.equals(rater.getId()))
                .toList();
    }
}
