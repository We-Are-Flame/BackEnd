package com.backend.repository.notification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class EmittersRepository {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    public SseEmitter getEmitter(Long userId) {
        return emitters.get(userId);
    }

    public void removeEmitter(Long userId) {
        emitters.remove(userId);
    }
}
