package com.example.ordering_lecture.redis;

import com.example.ordering_lecture.alarm.repository.AlarmRepository;
import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Slf4j
public class RedisSubscriber implements MessageListener {
    private final AlarmRepository alarmRepository;

    public RedisSubscriber(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String[] strings = message.toString().split("_");
            String date =strings[0];
            String email = strings[1];
            String context = date+"_"+strings[2];
            SseEmitter emitter =alarmRepository.get(email);
            emitter.send(SseEmitter.event().name("message").data(context));
        } catch (Exception e) {
            log.error(e.toString());
            throw new OrTopiaException(ErrorCode.SSE_MESSAGE_SEND_ERROR);
        }
    }
}