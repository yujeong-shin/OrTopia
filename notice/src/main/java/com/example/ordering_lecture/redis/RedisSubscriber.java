package com.example.ordering_lecture.redis;

import com.example.ordering_lecture.alarm.repository.AlarmRepository;
import com.example.ordering_lecture.alarm.repository.LikeSellerRepository;
import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Service
@Slf4j
public class RedisSubscriber implements MessageListener {
    private final AlarmRepository alarmRepository;
    private final LikeSellerRepository likeSellerRepository;
    public RedisSubscriber(AlarmRepository alarmRepository, LikeSellerRepository likeSellerRepository) {
        this.alarmRepository = alarmRepository;
        this.likeSellerRepository = likeSellerRepository;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String[] strings = message.toString().split("_");
            String date =strings[0];
            String sellerEmail = strings[1];
            List<String> memberEmails = likeSellerRepository.get(sellerEmail);
            String context = date+"_"+strings[2]+"_"+strings[3];
            for(String memberEmail : memberEmails) {
                SseEmitter emitter = alarmRepository.get(memberEmail);
                emitter.send(SseEmitter.event().name("message").data(context));
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new OrTopiaException(ErrorCode.SSE_MESSAGE_SEND_ERROR);
        }
    }
}