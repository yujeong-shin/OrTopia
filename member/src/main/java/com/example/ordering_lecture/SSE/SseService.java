package com.example.ordering_lecture.SSE;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class SseService {
    private static final AtomicLong counter = new AtomicLong();
    private final EmitterRepository emitterRepository;

    public SseService(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter add(String email, SseEmitter emitter) {
        emitterRepository.save(email, emitter);
        emitter.onCompletion(() -> emitterRepository.deleteByEmail(email));
        emitter.onTimeout(() -> {
            emitterRepository.get(email).ifPresent(SseEmitter::complete);
            emitterRepository.deleteByEmail(email);
        });
        return emitter;
    }
}