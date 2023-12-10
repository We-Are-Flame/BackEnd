package com.backend.service.notification;

import com.backend.entity.notification.Notification;
import com.backend.entity.notification.NotificationType;
import com.backend.entity.user.User;
import com.backend.repository.notification.EmittersRepository;
import com.backend.repository.notification.NotificationRepository;
import java.io.IOException;
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

    public SseEmitter subscribe(Long userId) {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name(CONNECTION_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        emittersRepository.addEmitter(userId, sseEmitter);

        sseEmitter.onCompletion(() -> emittersRepository.removeEmitter(userId));
        sseEmitter.onTimeout(() -> emittersRepository.removeEmitter(userId));
        sseEmitter.onError((e) -> emittersRepository.removeEmitter(userId));

        return sseEmitter;
    }

    public void sendNotification(User user, NotificationType type, String meetingName) {
        String content = NotificationFormatter.format(type, meetingName);
        log.info("content = {}", content);
        Notification notification = buildNotification(user, type, content);
        notificationRepository.save(notification);

        SseEmitter emitter = emittersRepository.getEmitter(user.getId());

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(type.toString()).data(content));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Notification buildNotification(User user, NotificationType type, String content) {
        return Notification.builder()
                .user(user)
                .type(type)
                .content(content)
                .isRead(false)
                .build();
    }
}
