package com.example.ordering_lecture.alarm.service;

import com.example.ordering_lecture.alarm.dto.LikeSellerResponseDto;
import com.example.ordering_lecture.alarm.repository.AlarmRepository;
import com.example.ordering_lecture.alarm.repository.LikeSellerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Component
@Slf4j
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

    public void addSellerData(String email, List<LikeSellerResponseDto> sellerEmails) {
        for(LikeSellerResponseDto likeSellerResponseDto : sellerEmails){
            likeSellerRepository.save(likeSellerResponseDto.getSellerEmail(),email);
        }
    }

    public void addSeller(String sellerEmail, String buyerEmail) {
        likeSellerRepository.save(sellerEmail,buyerEmail);
    }

    public void removeSeller(String sellerEmail, String buyerEmail) {
        likeSellerRepository.deleteByEmail(buyerEmail,sellerEmail);
    }

    public void removeConnect(String email) {
        log.info("현재 연결 상태"+email+" "+alarmRepository.containKey(email));
        alarmRepository.deleteByEmail(email);
        log.info("현재 연결 상태"+email+" "+alarmRepository.containKey(email));
    }
}
