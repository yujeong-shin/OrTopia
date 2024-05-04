package com.example.ordering_lecture.alarm.controller;

import com.example.ordering_lecture.alarm.service.AlarmService;
import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.feign.MemberServiceClient;
import com.example.ordering_lecture.redis.RedisPublisher;
import com.example.ordering_lecture.redis.RedisSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class AlarmController {
    private final AlarmService alarmService;
    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListener;
    private final MemberServiceClient memberServiceClient;
    @Autowired
    public AlarmController(AlarmService alarmService, RedisSubscriber redisSubscriber, RedisMessageListenerContainer redisMessageListener, MemberServiceClient memberServiceClient) {
        this.alarmService = alarmService;
        this.redisSubscriber = redisSubscriber;
        this.redisMessageListener = redisMessageListener;
        this.memberServiceClient = memberServiceClient;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestHeader("myEmail") String email){
        // 만료시간 설정
        SseEmitter emitter = new SseEmitter(30*60*1000L);
        // 연결 저장.
        alarmService.add(email,emitter);
        try {
            //log.info("sse 연결 성공");
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        }catch (IOException e){
            log.error(e.toString());
            throw new OrTopiaException(ErrorCode.SSE_CONNECTION_ERROR);
        }
        // 내가 좋아요 팔로우한 구매자들의 이메일 목록
        List<String> sellerEmails = memberServiceClient.searchEmailsBySellerId(email);
        alarmService.addSellerData(email,sellerEmails);

        // 내가 좋아요 한 구매자 채널의 구독.
        for(String sellerEmail : sellerEmails){
            ChannelTopic channel = new ChannelTopic(sellerEmail);
            redisMessageListener.addMessageListener(redisSubscriber, channel);
            log.info(email+"=> subscribe "+sellerEmail);
        }
        return ResponseEntity.ok(emitter);
    }
}