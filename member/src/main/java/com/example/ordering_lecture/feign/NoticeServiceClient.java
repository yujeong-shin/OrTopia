package com.example.ordering_lecture.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "ortopia-notice-service", url ="http://ortopia-notice-service.default.svc.cluster.local")
public interface NoticeServiceClient {

    // 즐겨찾기 시 해당 채널을 구독.
    @PostMapping("alarm/subscribe/{sellerEmail}/{buyerEmail}")
     String subscribeSellerEmail(@PathVariable("sellerEmail") String sellerEmail,@PathVariable("buyerEmail") String buyerEmail);

    // 즐겨찾기 시 해당 채널을 구독.
    @PostMapping("alarm/unsubscribe/{sellerEmail}/{buyerEmail}")
     String unsubscribeSellerEmail(@PathVariable("sellerEmail")String sellerEmail,@PathVariable("buyerEmail")String buyerEmail);
}
