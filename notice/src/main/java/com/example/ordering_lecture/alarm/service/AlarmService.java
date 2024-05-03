package com.example.ordering_lecture.alarm.service;

import com.example.ordering_lecture.alarm.repository.AlarmRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    public SseEmitter add(String email, SseEmitter emitter){
        alarmRepository.save(email,emitter);
        emitter.onCompletion(()->{
            alarmRepository.deleteByEmail(email);
        });
        emitter.onTimeout(()->{
            alarmRepository.get(email).complete();
        });
        return emitter;
    }

    SseEmitter get(String email){
        return alarmRepository.get(email);
    }

    boolean containKey(String email){
        return alarmRepository.containKey(email);
    }
}
