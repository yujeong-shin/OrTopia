package com.example.ordering_lecture.alarm.controller;

import com.example.ordering_lecture.alarm.dto.LikeSellerResponseDto;
import com.example.ordering_lecture.alarm.service.AlarmService;
import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.feign.MemberServiceClient;
import com.example.ordering_lecture.redis.RedisPublisher;
import com.example.ordering_lecture.redis.RedisService;
import com.example.ordering_lecture.redis.RedisSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class AlarmController {
    private final AlarmService alarmService;
    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListener;
    private final MemberServiceClient memberServiceClient;
    private final RedisService redisService;
    @Autowired
    public AlarmController(AlarmService alarmService, RedisSubscriber redisSubscriber, RedisMessageListenerContainer redisMessageListener, MemberServiceClient memberServiceClient, RedisService redisService) {
        this.alarmService = alarmService;
        this.redisSubscriber = redisSubscriber;
        this.redisMessageListener = redisMessageListener;
        this.memberServiceClient = memberServiceClient;
        this.redisService = redisService;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestHeader("myEmail") String email) {
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
        List<LikeSellerResponseDto> sellerEmails = memberServiceClient.searchEmailsBySellerId(email);
        alarmService.addSellerData(email,sellerEmails);

        // 내가 좋아요 한 구매자 채널의 구독.
        for(LikeSellerResponseDto likeSellerResponseDto : sellerEmails){
            ChannelTopic channel = new ChannelTopic(likeSellerResponseDto.getSellerEmail());
            redisMessageListener.addMessageListener(redisSubscriber, channel);
            log.info(email+"=> subscribe "+likeSellerResponseDto.getSellerEmail());
        }
        // 못받은 메시지 확인
        for(LikeSellerResponseDto likeSellerResponseDto : sellerEmails){
            List<String> lastEvents = redisService.getValues(likeSellerResponseDto.getSellerEmail());
            for(String lastEvent : lastEvents){
                String[] strings = lastEvent.split("_");
                if(Long.parseLong(strings[0])<likeSellerResponseDto.getEventId()){
                    // message send
                    try{
                        emitter.send(SseEmitter.event().name("message").data( strings[1]+"_"+strings[3]+"_"+strings[4]));
                    }catch (IOException e){
                        new OrTopiaException(ErrorCode.SSE_MESSAGE_SEND_ERROR);
                    }
                }
            }

        }
        return ResponseEntity.ok(emitter);
    }
    @PostMapping("alarm/subscribe/{sellerEmail}/{buyerEmail}")
    public String subscribeSellerEmail(@PathVariable("sellerEmail")String sellerEmail,@PathVariable("buyerEmail")String buyerEmail){
        ChannelTopic channelTopic = new ChannelTopic(sellerEmail);
        redisMessageListener.addMessageListener(redisSubscriber,channelTopic);
        log.info(sellerEmail+"채널 구독 성공");
        alarmService.addSeller(sellerEmail,buyerEmail);
        return "success subscribe";
    }
    @PostMapping("alarm/unsubscribe/{sellerEmail}/{buyerEmail}")
    public String unsubscribeSellerEmail(@PathVariable("sellerEmail")String sellerEmail,@PathVariable("buyerEmail")String buyerEmail){
        ChannelTopic channelTopic = new ChannelTopic(sellerEmail);
        redisMessageListener.removeMessageListener(redisSubscriber,channelTopic);
        log.info(sellerEmail+"채널 구독취소 성공");
        alarmService.removeSeller(sellerEmail,buyerEmail);
        return "success unsubscribe";
    }
}