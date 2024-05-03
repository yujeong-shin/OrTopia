package com.example.ordering_lecture.alarm.controller;

import com.example.ordering_lecture.alarm.service.AlarmService;
import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
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
import java.util.stream.Collectors;

@RestController
@Slf4j
public class AlarmController {
    private final AlarmService alarmService;
    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListener;
    @Autowired
    public AlarmController(AlarmService alarmService, RedisSubscriber redisSubscriber, RedisMessageListenerContainer redisMessageListener) {
        this.alarmService = alarmService;
        this.redisSubscriber = redisSubscriber;
        this.redisMessageListener = redisMessageListener;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestHeader("myEmail") String email){
        // 만료시간 설정
        SseEmitter emitter = new SseEmitter(30*60*1000L);
        // 연결 저장.
        alarmService.add(email,emitter);
        try {
            log.info("sse 연결 성공");
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        }catch (IOException e){
            log.error(e.toString());
            throw new OrTopiaException(ErrorCode.SSE_CONNECTION_ERROR);
        }
        // 통신된 채널.
        ChannelTopic channel = new ChannelTopic(email);
        redisMessageListener.addMessageListener(redisSubscriber, channel);

//        if(Boolean.TRUE.equals(redisTemplate2.hasKey(authentication.getName()))){
//            ListOperations<String,Object> valueOperations = redisTemplate2.opsForList();
//            System.out.println(valueOperations.size(authentication.getName()));
//            Long size = valueOperations.size(authentication.getName());
//            List<FeedBackNotificationRes> feedBackNotificationResList = valueOperations.range(authentication.getName(),0,size-1)
//                    .stream()
//                    .map(a->(FeedBackNotificationRes)a)
//                    .collect(Collectors.toList());
//            UserIdPassword userIdPassword = userRepository.findByEmail(authentication.getName()).orElseThrow(
//                    ()-> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
//            if(userIdPassword.getRole().equals(Role.MEMBER)){
//                for(FeedBackNotificationRes feedBackNotificationRes : feedBackNotificationResList) {
//                    sendLastInfoToMember(authentication.getName(), feedBackNotificationRes);
//                }
//            }else if(userIdPassword.getRole().equals(Role.TRAINER)){
//                for(FeedBackNotificationRes feedBackNotificationRes : feedBackNotificationResList) {
//                    sendLastInfoToTrainer(authentication.getName(), feedBackNotificationRes);
//                }
//            }
//            redisTemplate2.delete(authentication.getName());
//        }
        return ResponseEntity.ok(emitter);
    }
}