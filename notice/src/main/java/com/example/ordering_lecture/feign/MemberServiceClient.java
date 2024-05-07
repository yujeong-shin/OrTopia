package com.example.ordering_lecture.feign;

import com.example.ordering_lecture.alarm.dto.LikeSellerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ortopia-member-service", url ="http://ortopia-member-service.default.svc.cluster.local")
public interface MemberServiceClient {

    // 구매자의 email로 자신이 팔로우하는 판매자의 이메일을 검색.
    @GetMapping("/member/search/seller/emails/{email}")
    List<LikeSellerResponseDto> searchEmailsBySellerId(@PathVariable("email") String email);
}
