package com.example.ordering_lecture.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "notice-member-service", url ="http://ortopia-notice-service.default.svc.cluster.local")
public interface NoticeServiceClient {

    // 구매자의 email로 자신이 팔로우하는 판매자의 이메일을 검색.
    @PostMapping("alarm/subscribe/{sellerEmail}/{buyerEmail}")
     String subscribeSellerEmail(@PathVariable("sellerEmail") String sellerEmail,@PathVariable("buyerEmail") String buyerEmail);

    @PostMapping("alarm/unsubscribe/{sellerEmail}/{buyerEmail}")
     String unsubscribeSellerEmail(@PathVariable("sellerEmail")String sellerEmail,@PathVariable("buyerEmail")String buyerEmail);
}
