package com.example.ordering_lecture.alarm.service;

import com.example.ordering_lecture.alarm.repository.EmitterRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class AlarmService {
    private static final AtomicLong counter = new AtomicLong();
    private final EmitterRepository emitterRepository;

    public AlarmService(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
    }

    public SseEmitter add(String email,SseEmitter emitter){
        emitterRepository.save(email,emitter);
        emitter.onCompletion(()->{
            emitterRepository.deleteByEmail(email);
        });
        emitter.onTimeout(()->{
            emitterRepository.get(email).complete();
        });
        return emitter;
    }

    SseEmitter get(String email){
        return emitterRepository.get(email);
    }

    boolean containKey(String email){
        return emitterRepository.containKey(email);
    }
}
