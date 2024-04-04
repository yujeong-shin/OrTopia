package com.example.ordering_lecture.SSE;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseEmittersService {
    private final EmitterRepository emitterRepository;

    public SseEmittersService(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter add(String email, SseEmitter emitter) {
        emitterRepository.save(email, emitter);
        emitter.onCompletion(() -> emitterRepository.deleteByEmail(email));
        emitter.onTimeout(() -> {
            emitterRepository.get(email).ifPresent(SseEmitter::complete);
            emitterRepository.deleteByEmail(email); // Timeout 후에도 삭제
        });
        return emitter;
    }
}