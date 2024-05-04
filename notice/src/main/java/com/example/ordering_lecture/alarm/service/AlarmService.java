package com.example.ordering_lecture.alarm.service;

import com.example.ordering_lecture.alarm.repository.AlarmRepository;
import com.example.ordering_lecture.alarm.repository.LikeSellerRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Component
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final LikeSellerRepository likeSellerRepository;

    public AlarmService(AlarmRepository alarmRepository, LikeSellerRepository likeSellerRepository) {
        this.alarmRepository = alarmRepository;
        this.likeSellerRepository = likeSellerRepository;
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

    public void addSellerData(String email, List<String> sellerEmails) {
        for(String sellerEmail : sellerEmails){
            likeSellerRepository.save(sellerEmail,email);
        }
    }
}
