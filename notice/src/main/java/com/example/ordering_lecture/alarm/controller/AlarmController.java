package com.example.ordering_lecture.alarm.controller;

import com.example.ordering_lecture.alarm.service.AlarmService;
import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class AlarmController {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final AlarmService alarmService;
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    // sse 연결과 채널 생성
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestHeader("myEmail") String email){
        // 만료시간 설정
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        alarmService.add(email,emitter);
        try {
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        }catch (IOException e){
            //TODO : 추후 Error 코드 변경
            throw new OrTopiaException(ErrorCode.ALARM_CONNECT_ERROR);
        }
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
